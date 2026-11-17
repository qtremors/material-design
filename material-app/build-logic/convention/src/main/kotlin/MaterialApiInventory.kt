import java.io.File
import java.util.zip.ZipFile

/**
 * Builds a deterministic inventory of the public API declared in an artifact's Kotlin sources.
 *
 * AndroidX sources follow the official Kotlin style: one declaration per line, top-level
 * declarations at column zero, and members indented four spaces. That makes a line-oriented
 * reader reliable enough to enumerate top-level declarations and the members of public objects
 * and classes without a Kotlin compiler on the build classpath. A declaration without a
 * visibility modifier is public, which covers libraries not compiled in explicit-API mode.
 */
internal object MaterialApiInventory {

    data class Symbol(
        val fqName: String,
        val kind: String,
        val stability: String,
        val optIns: Set<String>,
        val overloads: Int,
        val group: String,
        val members: Set<String>,
    )

    private data class Declaration(
        val fqName: String,
        val kind: String,
        val optIns: Set<String>,
        val file: String,
        val owner: String?,
        val name: String,
    )

    private val declarationPattern = Regex(
        """^(\s*)((?:@[\w.]+(?:\([^)]*\))?\s+)*)(?:(public|private|internal|protected)\s+)?((?:(?:inline|sealed|abstract|open|data|value|enum|annotation|expect|actual|operator|infix|suspend|fun|const|override|lateinit|tailrec|external)\s+)*)(fun|class|interface|object|val|var|typealias)\s+(?:<[^>]*>\s+)?((?:[\w.<>?, ]+\.)?)(`?[A-Za-z_]\w*`?)""",
    )
    private val experimentalAnnotation = Regex("""@(Experimental\w*Api)\b""")

    fun read(sourcesJar: File): List<Symbol> {
        val declarations = mutableListOf<Declaration>()
        ZipFile(sourcesJar).use { zip ->
            zip.entries().asSequence()
                .filter { !it.isDirectory && it.name.endsWith(".kt") }
                .sortedBy { it.name }
                .forEach { entry ->
                    val text = zip.getInputStream(entry).bufferedReader().readText()
                    declarations += parseFile(entry.name.substringAfterLast('/'), text)
                }
        }
        return merge(declarations)
    }

    private fun parseFile(fileName: String, text: String): List<Declaration> {
        val lines = text.lines()
        val packageName = lines.firstOrNull { it.startsWith("package ") }
            ?.removePrefix("package ")?.trim().orEmpty()
        val result = mutableListOf<Declaration>()
        var currentOwner: String? = null

        // Implementation-detail packages are public to the compiler but are not library API.
        if (packageName.split('.').any { it == "internal" || it == "samples" }) return result

        lines.forEachIndexed { index, line ->
            val match = declarationPattern.find(line) ?: return@forEachIndexed
            val indent = match.groupValues[1].length
            if (indent != 0 && indent != 4) return@forEachIndexed
            // Libraries outside explicit-API mode omit `public`; any other visibility is not API.
            val visibility = match.groupValues[3]
            if (visibility.isNotEmpty() && visibility != "public") {
                if (indent == 0) currentOwner = null
                return@forEachIndexed
            }
            val modifiers = match.groupValues[4]
            // Overrides restate inherited API; `actual` declarations restate their common `expect`.
            if (modifiers.split(Regex("\\s+")).any { it == "override" || it == "actual" }) return@forEachIndexed
            val keyword = match.groupValues[5]
            val receiver = match.groupValues[6]
            val name = match.groupValues[7].trim('`')

            val annotationBlock = annotationsAbove(lines, index) + " " + match.groupValues[2]
            if (isHiddenDeprecation(lines, index, annotationBlock)) return@forEachIndexed
            val deprecated = "@Deprecated" in annotationBlock
            val optIns = experimentalAnnotation.findAll(annotationBlock).map { it.groupValues[1] }.toSet()
            val composable = "@Composable" in annotationBlock

            if (indent == 0) {
                val kind = when {
                    deprecated -> "deprecated"
                    keyword == "fun" && composable -> "composable"
                    keyword == "fun" -> "function"
                    keyword == "val" || keyword == "var" -> "property"
                    "annotation" in modifiers -> "annotation"
                    "enum" in modifiers -> "enum"
                    "value" in modifiers -> "value class"
                    else -> keyword
                }
                // Extension declarations are addressed by their simple name within the package.
                val fqName = "$packageName.$name"
                result += Declaration(fqName, kind, optIns, fileName, owner = null, name = name)
                currentOwner = if (keyword in setOf("class", "object", "interface") && receiver.isEmpty()) fqName else null
            } else if (currentOwner != null && !deprecated && receiver.isEmpty()) {
                result += Declaration("$currentOwner.$name", "member", optIns, fileName, currentOwner, name)
            }
        }
        return result
    }

    private fun annotationsAbove(lines: List<String>, index: Int): String {
        val collected = StringBuilder()
        var cursor = index - 1
        var budget = 40
        while (cursor >= 0 && budget-- > 0) {
            val trimmed = lines[cursor].trim()
            when {
                trimmed.isEmpty() -> break
                trimmed.startsWith("*") || trimmed.startsWith("/*") || trimmed.startsWith("//") -> Unit
                trimmed.startsWith("@") || trimmed.startsWith(")") || trimmed.endsWith(",") ||
                    trimmed.startsWith("\"") || trimmed.startsWith("level") || trimmed.startsWith("message") ||
                    trimmed.startsWith("replaceWith") || trimmed.startsWith("ReplaceWith") ||
                    trimmed.startsWith("imports") -> collected.append(' ').append(trimmed)
                else -> break
            }
            cursor--
        }
        return collected.toString()
    }

    private fun isHiddenDeprecation(lines: List<String>, index: Int, annotationBlock: String): Boolean =
        "@Deprecated" in annotationBlock && "DeprecationLevel.HIDDEN" in annotationBlock

    private fun merge(declarations: List<Declaration>): List<Symbol> {
        val topLevel = declarations.filter { it.owner == null }.groupBy { it.fqName }
        val members = declarations.filter { it.owner != null }.groupBy { it.owner!! }

        return topLevel.map { (fqName, group) ->
            val live = group.filter { it.kind != "deprecated" }
            val effective = live.ifEmpty { group }
            val kind = if (live.isEmpty()) "deprecated" else live.first().kind
            val annotated = effective.count { it.optIns.isNotEmpty() }
            val stability = when (annotated) {
                0 -> "stable"
                effective.size -> "experimental"
                else -> "mixed"
            }
            Symbol(
                fqName = fqName,
                kind = kind,
                stability = stability,
                optIns = effective.flatMap { it.optIns }.toSortedSet(),
                overloads = effective.size,
                group = effective.first().file.removeSuffix(".kt").substringBefore('.'),
                members = members[fqName].orEmpty().map { it.name }.toSortedSet(),
            )
        }.sortedBy { it.fqName }
    }

    fun toJson(artifact: String, version: String, symbols: List<Symbol>): String = buildString {
        appendLine("{")
        appendLine("  \"artifact\": ${quote(artifact)},")
        appendLine("  \"version\": ${quote(version)},")
        appendLine("  \"symbols\": [")
        symbols.forEachIndexed { index, symbol ->
            append("    {\"fqName\": ${quote(symbol.fqName)}, \"kind\": ${quote(symbol.kind)}, ")
            append("\"stability\": ${quote(symbol.stability)}, ")
            append("\"optIns\": ${symbol.optIns.joinToString(prefix = "[", postfix = "]") { quote(it) }}, ")
            append("\"overloads\": ${symbol.overloads}, \"group\": ${quote(symbol.group)}, ")
            append("\"members\": ${symbol.members.joinToString(prefix = "[", postfix = "]") { quote(it) }}}")
            appendLine(if (index == symbols.lastIndex) "" else ",")
        }
        appendLine("  ]")
        appendLine("}")
    }

    fun quote(value: String): String = buildString {
        append('"')
        value.forEach { char ->
            when (char) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                else -> append(char)
            }
        }
        append('"')
    }
}

import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MaterialApiInventoryTest {

    private fun inventoryOf(vararg files: Pair<String, String>): Map<String, MaterialApiInventory.Symbol> {
        val jar = Files.createTempFile("sources", ".jar").toFile()
        ZipOutputStream(jar.outputStream()).use { zip ->
            files.forEach { (name, text) ->
                zip.putNextEntry(ZipEntry("commonMain/androidx/example/$name"))
                zip.write(text.toByteArray())
                zip.closeEntry()
            }
        }
        return MaterialApiInventory.read(jar).associateBy { it.fqName.substringAfterLast('.') }
    }

    @Test
    fun `wrapped type parameters still produce the declaration`() {
        val symbols = inventoryOf(
            "Pane.kt" to """
                package androidx.example

                @ExperimentalExampleApi
                @Composable
                fun <
                    RoleT : PaneScaffoldRole,
                    ScaffoldValueT : PaneScaffoldValue<RoleT>,
                > ExtendedPaneScaffoldPaneScope<RoleT, ScaffoldValueT>.AnimatedPane(
                    modifier: Modifier = Modifier,
                ) {}
            """.trimIndent(),
        )
        val pane = symbols.getValue("AnimatedPane")
        assertEquals("composable", pane.kind)
        assertEquals("experimental", pane.stability)
    }

    @Test
    fun `implicit public, explicit private, and hidden deprecations are classified`() {
        val symbols = inventoryOf(
            "Widgets.kt" to """
                package androidx.example

                @Composable
                fun Visible() {}

                private fun Hidden() {}

                @Deprecated("Gone", level = DeprecationLevel.HIDDEN)
                @Composable
                fun Removed() {}

                @Deprecated("Use Visible")
                @Composable
                fun Old() {}

                object VisibleDefaults {
                    val Padding = 4
                    private val Secret = 1
                }
            """.trimIndent(),
        )
        assertTrue("Visible" in symbols)
        assertTrue("Hidden" !in symbols)
        assertTrue("Removed" !in symbols)
        assertEquals("deprecated", symbols.getValue("Old").kind)
        assertEquals(setOf("Padding"), symbols.getValue("VisibleDefaults").members)
    }
}

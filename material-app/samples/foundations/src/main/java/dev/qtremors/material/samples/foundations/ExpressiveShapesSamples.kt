package dev.qtremors.material.samples.foundations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.expressiveSpring
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

data class ExpressiveShapeItem(
    val name: String,
    val shape: Shape,
    val category: String,
)

object MaterialExpressiveShapes {
    // 1. Basic Shapes
    val Circle: Shape = CircleShape
    val Square: Shape = RoundedCornerShape(12.dp)
    val Slanted: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.25f, 0f)
        lineTo(w * 0.98f, 0f)
        lineTo(w * 0.75f, h)
        lineTo(w * 0.02f, h)
        close()
    }
    val Arch: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(0f, h)
        lineTo(0f, h * 0.45f)
        arcTo(Rect(0f, 0f, w, h * 0.9f), 180f, 180f, false)
        lineTo(w, h)
        close()
    }
    val Semicircle: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(0f, h)
        arcTo(Rect(0f, 0f, w, h * 2f), 180f, 180f, false)
        close()
    }

    // 2. Geometric Shapes
    val Oval: Shape = GenericShape { size, _ ->
        addOval(Rect(0f, size.height * 0.15f, size.width, size.height * 0.85f))
    }
    val Pill: Shape = RoundedCornerShape(50)
    val Triangle: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, h * 0.05f)
        lineTo(w * 0.95f, h * 0.95f)
        lineTo(w * 0.05f, h * 0.95f)
        close()
    }
    val Arrow: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, 0f)
        lineTo(w, h * 0.48f)
        lineTo(w * 0.7f, h * 0.48f)
        lineTo(w * 0.7f, h)
        lineTo(w * 0.3f, h)
        lineTo(w * 0.3f, h * 0.48f)
        lineTo(0f, h * 0.48f)
        close()
    }
    val Fan: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.08f, h * 0.95f)
        lineTo(0f, h * 0.4f)
        arcTo(Rect(0f, 0f, w, h * 0.8f), 180f, 180f, false)
        lineTo(w * 0.92f, h * 0.95f)
        close()
    }

    // 3. Polygons
    val Diamond: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, 0f)
        lineTo(w, h * 0.5f)
        lineTo(w * 0.5f, h)
        lineTo(0f, h * 0.5f)
        close()
    }
    val Clamshell: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.2f, 0f)
        lineTo(w * 0.8f, 0f)
        lineTo(w, h * 0.5f)
        lineTo(w * 0.8f, h)
        lineTo(w * 0.2f, h)
        lineTo(0f, h * 0.5f)
        close()
    }
    val Pentagon: Shape = createPolygonShape(5)
    val Gem: Shape = createPolygonShape(6)

    // 4. Suns & Stars
    val VerySunny: Shape = createStarShape(numPoints = 8, innerRadiusRatio = 0.62f)
    val Sunny: Shape = createStarShape(numPoints = 8, innerRadiusRatio = 0.78f)

    // 5. Cookies
    val Cookie4Sided: Shape = createCookieShape(4)
    val Cookie6Sided: Shape = createCookieShape(6)
    val Cookie7Sided: Shape = createCookieShape(7)
    val Cookie9Sided: Shape = createCookieShape(9)
    val Cookie12Sided: Shape = createCookieShape(12)

    // 6. Clovers & Floral
    val Clover4Leaf: Shape = createCloverShape(4)
    val Clover8Leaf: Shape = createCloverShape(8)
    val Flower: Shape = createCloverShape(12)
    val Puffy: Shape = createCloverShape(6)
    val PuffyDiamond: Shape = createCloverShape(4)

    // 7. Bursts & Booms
    val Burst: Shape = createStarShape(numPoints = 12, innerRadiusRatio = 0.45f)
    val SoftBurst: Shape = createStarShape(numPoints = 12, innerRadiusRatio = 0.70f)
    val Boom: Shape = createStarShape(numPoints = 16, innerRadiusRatio = 0.35f)
    val SoftBoom: Shape = createStarShape(numPoints = 16, innerRadiusRatio = 0.65f)

    // 8. Novelty
    val Ghostish: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.15f, h * 0.95f)
        cubicTo(0f, h * 0.7f, 0f, h * 0.15f, w * 0.5f, 0f)
        cubicTo(w, h * 0.15f, w, h * 0.7f, w * 0.85f, h * 0.95f)
        cubicTo(w * 0.65f, h * 0.82f, w * 0.35f, h * 0.82f, w * 0.15f, h * 0.95f)
        close()
    }
    val PixelCircle: Shape = GenericShape { size, _ ->
        val u = size.width / 8f
        addRect(Rect(u * 2, 0f, u * 6, size.height))
        addRect(Rect(0f, u * 2, size.width, size.height - u * 2))
        addRect(Rect(u, u, size.width - u, size.height - u))
    }
    val PixelTriangle: Shape = GenericShape { size, _ ->
        val u = size.width / 6f
        addRect(Rect(u * 2, 0f, u * 4, u * 2))
        addRect(Rect(u, u * 2, u * 5, u * 4))
        addRect(Rect(0f, u * 4, size.width, size.height))
    }
    val Bun: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        addRoundRect(RoundRect(0f, 0f, w, h * 0.44f, CornerRadius(h * 0.22f)))
        addRoundRect(RoundRect(0f, h * 0.56f, w, h, CornerRadius(h * 0.22f)))
    }
    val Heart: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, h * 0.90f)
        cubicTo(w * 0.05f, h * 0.62f, 0f, h * 0.35f, 0f, h * 0.22f)
        cubicTo(0f, h * 0.05f, w * 0.25f, -h * 0.02f, w * 0.5f, h * 0.20f)
        cubicTo(w * 0.75f, -h * 0.02f, w, h * 0.05f, w, h * 0.22f)
        cubicTo(w, h * 0.35f, w * 0.95f, h * 0.62f, w * 0.5f, h * 0.90f)
        close()
    }

    val AllShapes = listOf(
        ExpressiveShapeItem("Circle", Circle, "Basic"),
        ExpressiveShapeItem("Square", Square, "Basic"),
        ExpressiveShapeItem("Slanted", Slanted, "Basic"),
        ExpressiveShapeItem("Arch", Arch, "Basic"),
        ExpressiveShapeItem("Semicircle", Semicircle, "Basic"),
        ExpressiveShapeItem("Oval", Oval, "Geometric"),
        ExpressiveShapeItem("Pill", Pill, "Geometric"),
        ExpressiveShapeItem("Triangle", Triangle, "Geometric"),
        ExpressiveShapeItem("Arrow", Arrow, "Geometric"),
        ExpressiveShapeItem("Fan", Fan, "Geometric"),
        ExpressiveShapeItem("Diamond", Diamond, "Polygons"),
        ExpressiveShapeItem("Clamshell", Clamshell, "Polygons"),
        ExpressiveShapeItem("Pentagon", Pentagon, "Polygons"),
        ExpressiveShapeItem("Gem", Gem, "Polygons"),
        ExpressiveShapeItem("Very sunny", VerySunny, "Suns"),
        ExpressiveShapeItem("Sunny", Sunny, "Suns"),
        ExpressiveShapeItem("4-sided cookie", Cookie4Sided, "Cookies"),
        ExpressiveShapeItem("6-sided cookie", Cookie6Sided, "Cookies"),
        ExpressiveShapeItem("7-sided cookie", Cookie7Sided, "Cookies"),
        ExpressiveShapeItem("9-sided cookie", Cookie9Sided, "Cookies"),
        ExpressiveShapeItem("12-sided cookie", Cookie12Sided, "Cookies"),
        ExpressiveShapeItem("4-leaf clover", Clover4Leaf, "Clovers"),
        ExpressiveShapeItem("8-leaf clover", Clover8Leaf, "Clovers"),
        ExpressiveShapeItem("Burst", Burst, "Bursts"),
        ExpressiveShapeItem("Soft burst", SoftBurst, "Bursts"),
        ExpressiveShapeItem("Boom", Boom, "Booms"),
        ExpressiveShapeItem("Soft boom", SoftBoom, "Booms"),
        ExpressiveShapeItem("Flower", Flower, "Floral"),
        ExpressiveShapeItem("Puffy", Puffy, "Floral"),
        ExpressiveShapeItem("Puffy diamond", PuffyDiamond, "Floral"),
        ExpressiveShapeItem("Ghost-ish", Ghostish, "Novelty"),
        ExpressiveShapeItem("Pixel circle", PixelCircle, "Novelty"),
        ExpressiveShapeItem("Pixel triangle", PixelTriangle, "Novelty"),
        ExpressiveShapeItem("Bun", Bun, "Novelty"),
        ExpressiveShapeItem("Heart", Heart, "Novelty"),
    )

    private fun createPolygonShape(sides: Int): Shape = GenericShape { size, _ ->
        val radius = min(size.width, size.height) * 0.48f
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val angleStep = (2 * PI / sides).toFloat()
        val startAngle = (-PI / 2).toFloat()

        for (i in 0 until sides) {
            val angle = startAngle + i * angleStep
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    private fun createStarShape(numPoints: Int, innerRadiusRatio: Float): Shape = GenericShape { size, _ ->
        val outerRadius = min(size.width, size.height) * 0.48f
        val innerRadius = outerRadius * innerRadiusRatio
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val totalPoints = numPoints * 2
        val angleStep = (2 * PI / totalPoints).toFloat()
        val startAngle = (-PI / 2).toFloat()

        for (i in 0 until totalPoints) {
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val angle = startAngle + i * angleStep
            val x = centerX + r * cos(angle)
            val y = centerY + r * sin(angle)
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    private fun createCookieShape(sides: Int): Shape = GenericShape { size, _ ->
        val radius = min(size.width, size.height) * 0.48f
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val angleStep = (2 * PI / sides).toFloat()

        for (i in 0 until sides) {
            val angle = i * angleStep
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    private fun createCloverShape(petals: Int): Shape = GenericShape { size, _ ->
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = min(size.width, size.height) * 0.48f
        val angleStep = (2 * PI / petals).toFloat()

        for (i in 0 until petals) {
            val angle = i * angleStep
            val petalRadius = radius * 0.38f
            val petalCenterX = centerX + (radius - petalRadius) * cos(angle)
            val petalCenterY = centerY + (radius - petalRadius) * sin(angle)
            addOval(Rect(petalCenterX - petalRadius, petalCenterY - petalRadius, petalCenterX + petalRadius, petalCenterY + petalRadius))
        }
    }
}

@Composable
fun ExpressiveShapesSample(modifier: Modifier = Modifier) {
    var selectedStartIndex by remember { mutableIntStateOf(0) } // Circle
    var selectedEndIndex by remember { mutableIntStateOf(16) }   // 4-sided cookie
    var selectedCategory by remember { mutableStateOf("All") }
    var morphProgress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    val reducedMotion = LocalReducedMotion.current
    val animatedProgress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else morphProgress,
        animationSpec = if (reducedMotion) tween(300) else expressiveSpring(),
        label = "morph progress animation",
    )

    val categories = listOf("All", "Basic", "Geometric", "Polygons", "Suns", "Cookies", "Clovers", "Bursts", "Floral", "Novelty")

    val filteredShapes = remember(selectedCategory) {
        if (selectedCategory == "All") {
            MaterialExpressiveShapes.AllShapes
        } else {
            MaterialExpressiveShapes.AllShapes.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Section 1: Shape Morphing Studio
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Shape Morphing Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Material 3 Expressive shapes & interpolation",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text(
                            text = "35 Shapes",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }

                // Interactive Morphing Stage (3 columns distributed evenly across 100% width)
                val startItem = MaterialExpressiveShapes.AllShapes.getOrElse(selectedStartIndex) { MaterialExpressiveShapes.AllShapes.first() }
                val endItem = MaterialExpressiveShapes.AllShapes.getOrElse(selectedEndIndex) { MaterialExpressiveShapes.AllShapes.last() }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Start Shape Box
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                                    .clipToBounds(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .background(MaterialTheme.colorScheme.primary, startItem.shape),
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = startItem.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "Source",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    // Morph Output Box
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shadowElevation = 2.dp,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                                    .clipToBounds(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = if (animatedProgress < 0.5f) startItem.shape else endItem.shape,
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.85f),
                                        modifier = Modifier.size(32.dp),
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${(animatedProgress * 100).toInt()}%",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.secondary,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Morphing",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    // End Target Shape Box
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                                    .clipToBounds(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .background(MaterialTheme.colorScheme.tertiary, endItem.shape),
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = endItem.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "Target",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }

                // Action Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = { isAnimating = !isAnimating },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = if (isAnimating) Icons.Default.Refresh else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isAnimating) "Reset Animation" else "Animate Morph")
                    }
                }

                if (!isAnimating) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Manual Slider", style = MaterialTheme.typography.labelMedium)
                            Text("${(morphProgress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = morphProgress,
                            onValueChange = { morphProgress = it },
                            valueRange = 0f..1f,
                        )
                    }
                }
            }
        }

        // Section 2: Catalog of Shapes (3 per row, filling 100% of available space)
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Material 3 Expressive Shape Library",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Tap any shape to set Source (Green), tap another to set Target (Pink).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // Category Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                    )
                }
            }

            // 3 Shapes per row grid filling all available width
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                filteredShapes.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        for (item in rowItems) {
                            val originalIndex = MaterialExpressiveShapes.AllShapes.indexOf(item)
                            val isStart = selectedStartIndex == originalIndex
                            val isEnd = selectedEndIndex == originalIndex

                            Surface(
                                onClick = {
                                    if (isStart) {
                                        selectedEndIndex = (originalIndex + 5) % MaterialExpressiveShapes.AllShapes.size
                                    } else {
                                        selectedStartIndex = originalIndex
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = when {
                                    isStart -> MaterialTheme.colorScheme.primaryContainer
                                    isEnd -> MaterialTheme.colorScheme.tertiaryContainer
                                    else -> MaterialTheme.colorScheme.surfaceContainer
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isStart || isEnd) 2.dp else 1.dp,
                                    color = when {
                                        isStart -> MaterialTheme.colorScheme.primary
                                        isEnd -> MaterialTheme.colorScheme.tertiary
                                        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    },
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(112.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 6.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clipToBounds(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    color = when {
                                                        isStart -> MaterialTheme.colorScheme.primary
                                                        isEnd -> MaterialTheme.colorScheme.tertiary
                                                        else -> MaterialTheme.colorScheme.onSurface
                                                    },
                                                    shape = item.shape,
                                                ),
                                        )
                                    }
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = if (isStart || isEnd) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isStart -> MaterialTheme.colorScheme.onPrimaryContainer
                                            isEnd -> MaterialTheme.colorScheme.onTertiaryContainer
                                            else -> MaterialTheme.colorScheme.onSurface
                                        },
                                    )
                                }
                            }
                        }
                        // Fill remaining spaces in last row to maintain alignment
                        if (rowItems.size < 3) {
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

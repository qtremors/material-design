package dev.qtremors.material.samples.foundations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.expressiveSpring
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class ExpressiveShapeItem(
    val name: String,
    val shape: Shape,
    val category: String,
)

object MaterialExpressiveShapes {
    // Row 1
    val Circle: Shape = CircleShape
    val Square: Shape = RoundedCornerShape(24.dp)
    val Slanted: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.25f, 0f)
        lineTo(w * 0.95f, 0f)
        lineTo(w * 0.75f, h)
        lineTo(w * 0.05f, h)
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

    // Row 2
    val Oval: Shape = GenericShape { size, _ ->
        addOval(Rect(0f, size.height * 0.1f, size.width, size.height * 0.9f))
    }
    val Pill: Shape = RoundedCornerShape(50)
    val Triangle: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, h * 0.05f)
        lineTo(w * 0.95f, h * 0.92f)
        lineTo(w * 0.05f, h * 0.92f)
        close()
    }
    val Arrow: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, 0f)
        lineTo(w, h * 0.5f)
        lineTo(w * 0.75f, h * 0.5f)
        lineTo(w * 0.75f, h)
        lineTo(w * 0.25f, h)
        lineTo(w * 0.25f, h * 0.5f)
        lineTo(0f, h * 0.5f)
        close()
    }
    val Fan: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(0f, h)
        lineTo(0f, h * 0.3f)
        arcTo(Rect(-w * 0.5f, -h * 0.5f, w * 1.5f, h * 1.5f), 210f, 120f, false)
        lineTo(w, h)
        close()
    }

    // Row 3
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
        moveTo(w * 0.15f, 0f)
        lineTo(w * 0.85f, 0f)
        lineTo(w, h * 0.5f)
        lineTo(w * 0.85f, h)
        lineTo(w * 0.15f, h)
        lineTo(0f, h * 0.5f)
        close()
    }
    val Pentagon: Shape = createPolygonShape(5)
    val Gem: Shape = createPolygonShape(6)
    val VerySunny: Shape = createStarShape(numPoints = 8, innerRadiusRatio = 0.65f)

    // Row 4
    val Sunny: Shape = createStarShape(numPoints = 8, innerRadiusRatio = 0.8f)
    val Cookie4Sided: Shape = createCookieShape(4)
    val Cookie6Sided: Shape = createCookieShape(6)
    val Cookie7Sided: Shape = createCookieShape(7)
    val Cookie9Sided: Shape = createCookieShape(9)

    // Row 5
    val Cookie12Sided: Shape = createCookieShape(12)
    val Clover4Leaf: Shape = createCloverShape(4)
    val Clover8Leaf: Shape = createCloverShape(8)
    val Burst: Shape = createStarShape(numPoints = 12, innerRadiusRatio = 0.45f)
    val SoftBurst: Shape = createStarShape(numPoints = 12, innerRadiusRatio = 0.7f)

    // Row 6
    val Boom: Shape = createStarShape(numPoints = 16, innerRadiusRatio = 0.35f)
    val SoftBoom: Shape = createStarShape(numPoints = 16, innerRadiusRatio = 0.65f)
    val Flower: Shape = createCloverShape(12)
    val Puffy: Shape = createCloverShape(6)
    val PuffyDiamond: Shape = createCloverShape(4)

    // Row 7
    val Ghostish: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.2f, h)
        cubicTo(0f, h * 0.8f, 0f, h * 0.2f, w * 0.5f, 0f)
        cubicTo(w, h * 0.2f, w, h * 0.8f, w * 0.8f, h)
        cubicTo(w * 0.6f, h * 0.85f, w * 0.4f, h * 0.85f, w * 0.2f, h)
        close()
    }
    val PixelCircle: Shape = GenericShape { size, _ ->
        val unit = size.width / 8f
        addRect(Rect(unit * 2, 0f, unit * 6, size.height))
        addRect(Rect(0f, unit * 2, size.width, unit * 6))
        addRect(Rect(unit, unit, unit * 7, unit * 7))
    }
    val PixelTriangle: Shape = GenericShape { size, _ ->
        val u = size.width / 6f
        addRect(Rect(u * 2, 0f, u * 4, u))
        addRect(Rect(u, u, u * 5, u * 3f))
        addRect(Rect(0f, u * 3f, size.width, size.height))
    }
    val Bun: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        addRoundRect(RoundRect(0f, 0f, w, h * 0.45f, CornerRadius(h * 0.2f)))
        addRoundRect(RoundRect(0f, h * 0.55f, w, h, CornerRadius(h * 0.2f)))
    }
    val Heart: Shape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, h * 0.85f)
        cubicTo(w * 0.1f, h * 0.6f, 0f, h * 0.35f, 0f, h * 0.22f)
        cubicTo(0f, h * 0.05f, w * 0.25f, -h * 0.05f, w * 0.5f, h * 0.2f)
        cubicTo(w * 0.75f, -h * 0.05f, w, h * 0.05f, w, h * 0.22f)
        cubicTo(w, h * 0.35f, w * 0.9f, h * 0.6f, w * 0.5f, h * 0.85f)
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
        val radius = Math.min(size.width, size.height) / 2f
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
        val outerRadius = Math.min(size.width, size.height) / 2f
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
        val radius = Math.min(size.width, size.height) / 2f
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val angleStep = (2 * PI / sides).toFloat()

        for (i in 0 until sides) {
            val angle = i * angleStep
            val x = centerX + radius * 0.9f * cos(angle)
            val y = centerY + radius * 0.9f * sin(angle)
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    private fun createCloverShape(petals: Int): Shape = GenericShape { size, _ ->
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = Math.min(size.width, size.height) / 2f
        val angleStep = (2 * PI / petals).toFloat()

        for (i in 0 until petals) {
            val angle = i * angleStep
            val petalCenterX = centerX + radius * 0.45f * cos(angle)
            val petalCenterY = centerY + radius * 0.45f * sin(angle)
            addOval(Rect(petalCenterX - radius * 0.4f, petalCenterY - radius * 0.4f, petalCenterX + radius * 0.4f, petalCenterY + radius * 0.4f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpressiveShapesSample(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var targetMorphIndex by remember { mutableIntStateOf(16) } // Sunny
    var morphProgress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    val reducedMotion = LocalReducedMotion.current
    val animatedProgress by animateFloatAsState(
        targetValue = if (isAnimating) 1f else morphProgress,
        animationSpec = expressiveSpring(),
        label = "morph progress animation",
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Section 1: Animated Shape Morphing Studio
        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Shape Morphing Studio", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Morphing seamlessly interpolates between any two Material 3 Expressive shapes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    Modifier.fillMaxWidth().height(140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Start Shape
                    val startItem = MaterialExpressiveShapes.AllShapes[selectedIndex]
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier.size(72.dp).background(MaterialTheme.colorScheme.primary, startItem.shape),
                            contentAlignment = Alignment.Center
                        ) {}
                        Spacer(Modifier.height(6.dp))
                        Text(startItem.name, style = MaterialTheme.typography.labelSmall)
                    }

                    // Morphing Canvas Output
                    val endItem = MaterialExpressiveShapes.AllShapes[targetMorphIndex]
                    Box(
                        Modifier.size(96.dp)
                            .background(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                if (animatedProgress < 0.5f) startItem.shape else endItem.shape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${(animatedProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    // End Shape
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier.size(72.dp).background(MaterialTheme.colorScheme.secondary, endItem.shape),
                            contentAlignment = Alignment.Center
                        ) {}
                        Spacer(Modifier.height(6.dp))
                        Text(endItem.name, style = MaterialTheme.typography.labelSmall)
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { isAnimating = !isAnimating },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isAnimating) "Reset Morph" else "Animate Morphing")
                    }
                }

                if (!isAnimating) {
                    Text("Manual Morph Progress", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = morphProgress,
                        onValueChange = { morphProgress = it },
                        valueRange = 0f..1f
                    )
                }
            }
        }

        // Section 2: 35 Official Material 3 Expressive Shapes Grid
        Text("35 Official Material 3 Expressive Shapes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Tap any shape to select it for morphing.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // 5-Column Responsive Grid matching user's reference image
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 5
        ) {
            MaterialExpressiveShapes.AllShapes.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val isTarget = targetMorphIndex == index

                Column(
                    modifier = Modifier
                        .width(64.dp)
                        .clickable {
                            if (selectedIndex == index) {
                                targetMorphIndex = (index + 5) % MaterialExpressiveShapes.AllShapes.size
                            } else {
                                selectedIndex = index
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .border(
                                width = if (isSelected || isTarget) 3.dp else 1.dp,
                                color = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isTarget -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    shape = item.shape
                                )
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        item.name,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.85f
                    )
                }
            }
        }
    }
}

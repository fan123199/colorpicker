package im.fdx.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun AlphaSlider(
    color: Color, //color without alpha
    alpha: Float,
    onAlphaChanged: (Float) -> Unit
) {

    var innerAlpha by remember {
        mutableStateOf(alpha)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(key1 = Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        innerAlpha = (offset.x / size.width).coerceIn(0f, 1f)
                        onAlphaChanged(innerAlpha)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    innerAlpha = (change.position.x / size.width).coerceIn(0f, 1f)
                    onAlphaChanged(innerAlpha)
                }
            }
            .padding(28.dp, 16.dp)
            .height(sliderHeight)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .background(
                Brush.linearGradient(
                    listOf(
                        color.copy(alpha = 0f), color.copy(alpha = 1f)
                    )
                )
            ),
            onDraw = {
                drawArc(
                    color = Color.Transparent, //left
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(-size.height / 2, 0f),
                    size = Size(size.height, size.height)
                )
                drawArc(
                    color = color.copy(alpha = 1f), //right
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(size.width - size.height / 2, 0f),
                    size = Size(size.height, size.height)
                )
                drawCircle(
                    Color.White,
                    radius = size.height / 2 * 0.8f,
                    center = Offset(innerAlpha * size.width, size.height / 2f),
                    style = Stroke(width = size.height / 2 * 0.4f)
                )
            })
    }
}
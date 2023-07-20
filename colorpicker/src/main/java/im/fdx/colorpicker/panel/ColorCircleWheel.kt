package im.fdx.colorpicker.panel

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.toOffset
import im.fdx.colorpicker.hueColors
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

//https://en.wikipedia.org/wiki/Color_wheel
@Composable
fun ColorWheel(
    modifier: Modifier = Modifier,
    hue: Float,
    saturation: Float,
    value: Float,
    alpha: Float,
    onColorChanged: (Color, Float, Float) -> Unit
) {

    val scope = rememberCoroutineScope()
    var point = remember {
        Animatable(Offset(-1f, -1f), Offset.VectorConverter)
    }

    var hue1 by remember { mutableStateOf(hue) }
    var saturation1 by remember { mutableStateOf(saturation) }


    val currentColor = Color.hsv(hue = hue1, saturation = saturation1, value = value, alpha = alpha)
    onColorChanged(currentColor, hue1, saturation1)

    Canvas(modifier = modifier
        .onSizeChanged {
            if (point.value.x == -1f && point.value.y == -1f) {
                scope.launch {
                    println("hahahaahhahahaha: $hue, $saturation,  $value")
                    val r = it.width/ 2
                    point.snapTo(
                        Offset((saturation * cos(hue * PI /180)).toFloat() * r + r, (saturation * sin(hue * PI /180)).toFloat()* r + r)
                    ) //todo 极坐标
                }
            }
        }
        .pointerInput(Unit) {

            detectDragGestures(onDragStart = {
                Log.d("start", "start x: ${it.x}")
            }
            ) { change: PointerInputChange, dragAmount: Offset ->
                Log.d("DANTO", "dragged x: ${dragAmount.x}")
                Log.d("DANTO", "dragged y: ${dragAmount.y}")
//            change.position
                val position = change.position
                val r = size.width / 2
                val distance =
                    (position - size.center.toOffset()).getDistance() //这个3非常的hard, 但是不加的话，会遇到边缘，就会出现一些奇怪的颜色（一个是报错，一个因为插值会出现透明度）
                val newPosition = if (distance > r) {
                    (position - size.center.toOffset()).times(r / distance) + size.center.toOffset()
                } else
                    position

                val offsetToCenter = newPosition - size.center.toOffset()
                val newDistance = offsetToCenter.getDistance()
                Log.d("newPosition", "newPosition xy: ${newPosition.x},  ${newPosition.y}")
                scope.launch {
                    point.snapTo(newPosition)
                    val angle = Math.toDegrees(atan2(offsetToCenter.y.toDouble(), offsetToCenter.x.toDouble()))
                    val normalizedAngle = (angle + 360) % (360)
                    hue1 = normalizedAngle.toFloat()
                    saturation1 = (abs(newDistance / r)).coerceIn(0f, 1f)
                }
            }
        }
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val r = size.width / 2
                val distance = (offset - size.center.toOffset()).getDistance() //这个3非常的hard, 但是不加的话，会遇到边缘，就会出现一些奇怪的颜色（一个是报错，一个因为插值会出现透明度）
                Log.d("newPosition", "dis and r: ${distance},  ${r}")
                val newOffset = if (distance > r) {
                    (offset - size.center.toOffset()).times(r / distance) + size.center.toOffset()
                } else
                    offset
                Log.d("newPosition", "origin xy: ${offset.x},  ${offset.y}")
                Log.d("newPosition", "newPosition xy: ${newOffset.x},  ${newOffset.y}")
                val newOffsetToCenter = newOffset - size.center.toOffset()
                val newDistance = newOffsetToCenter.getDistance()
                scope.launch {
                    val angle = Math.toDegrees(atan2(newOffsetToCenter.y.toDouble(), newOffsetToCenter.x.toDouble()))
                    val normalizedAngle = (angle + 360) % (360)
                    hue1 = normalizedAngle.toFloat()
                    saturation1 = (abs(newDistance / r)).coerceIn(0f, 1f)
                    point.animateTo(newOffset)
                }
            }
        }

    ) {
        drawCircle(Brush.sweepGradient(hueColors))
        drawCircle(
            Brush.radialGradient(
                listOf(
                    Color.White,
                    Color.White.copy(alpha = 0f)
                )
            )
        )

        val r = size.maxDimension / 40
        drawCircle(
            Color.White,
            radius = r * 0.8f,
            center = point.value,
            style = Stroke(width = r * 0.4f)
        )
        drawCircle(
            Color(189, 189, 189),
            radius = r,
            center = point.value,
            style = Stroke(width = 2f)
        )

    }
}
package im.fdx.colorpicker.panel

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import im.fdx.colorpicker.sliderHeight
import kotlinx.coroutines.launch


fun Color.toHSV(): FloatArray {
    val hsv = floatArrayOf(0f, 0f, 0f)
    android.graphics.Color.colorToHSV(this.toArgb(), hsv)
    return hsv
}


@Composable
fun ColorRectPanel(
    modifier: Modifier = Modifier,
    hue: Float,
    saturation: Float,
    value: Float,
    alpha: Float,
    onColorChanged: (Color) -> Unit,
) {

    var saturation1 by remember { mutableStateOf(saturation) }
    var value1 by remember { mutableStateOf(value) }

    val currentColor = Color.hsv(hue = hue, saturation = saturation1, value = value1, alpha = alpha)
    onColorChanged(currentColor)

    val point = remember {
        Animatable(Offset(-1f, -1f), Offset.VectorConverter)
    }

    var r = with(LocalDensity.current) {
        sliderHeight.toPx() /2
    }
    val scope = rememberCoroutineScope()
    Canvas(modifier = modifier
        .onSizeChanged {
            if (point.value.x == -1f && point.value.y == -1f) {
                scope.launch {
                    println("hahahaahhahahaha $saturation1, $value1")
                    point.snapTo(Offset(it.width * saturation1, (it.height * (1 - value1))))
                }
            }
        }
        .pointerInput(Unit) {
            detectDragGestures(
            ) { change: PointerInputChange, dragAmount: Offset ->
                Log.d("DANTO", "dragged x: ${dragAmount.x}")
                Log.d("DANTO", "dragged y: ${dragAmount.y}")
                val x = change.position.x
                val y = change.position.y
                val newPosition =
                    Offset(
                        x.coerceIn(0f, size.width.toFloat()),
                        y.coerceIn(0f, size.height.toFloat())
                    )
                Log.d("newPosition", "newPosition xy: ${newPosition.x},  ${newPosition.y}")
                scope.launch {
                    point.snapTo(newPosition)
                    saturation1 = newPosition.x / size.width
                    value1 = 1 - newPosition.y / size.height
                }
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    println("in onPress ${offset}")
                    scope.launch {
                        println("in Scope")
                        saturation1 = offset.x / size.width
                        value1 = 1 - offset.y / size.height
                        point.animateTo(offset)
                    }
                },
                onTap = { offset ->

                })
        }

    ) {

        drawRect(
            Brush.linearGradient(
                arrayListOf(
                    Color.hsv(hue = hue, saturation = 0f, value = 1f),
                    Color.hsv(hue = hue, saturation = 1f, value = 1f),
                ),
                end = Offset(Float.POSITIVE_INFINITY, 0f)

            )
        )

        drawRect(
            Brush.linearGradient(
                arrayListOf(
                    Color.hsv(hue = hue, saturation = 0f, value = 1f),
                    Color.hsv(hue = hue, saturation = 0f, value = 0f),
                ),
                end = Offset(0f, Float.POSITIVE_INFINITY),
            ),

            blendMode = BlendMode.Multiply
        )

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
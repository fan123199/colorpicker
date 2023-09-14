package im.fdx.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun HueSlider(hue: Float, onHueChanged: (Float) -> Unit) {
    var hue1 by remember {
        mutableStateOf(hue)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(key1 = Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        hue1 = (offset.x / size.width * 360).coerceIn(0f, 360f)
                        onHueChanged(hue1)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    hue1 = (change.position.x / size.width * 360).coerceIn(0f, 360f)
                    onHueChanged(hue1)
                }
            }
            .padding(28.dp, 16.dp)
            .height(sliderHeight),
        horizontalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .background(Brush.linearGradient(hueColors), RectangleShape),
            onDraw = {
                drawArc(
                    color = Color.Red, //left
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(-size.height / 2, 0f),
                    size = Size(size.height, size.height)
                )
                drawArc(
                    color = Color.Red, //right
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(size.width -size.height / 2, 0f),
                    size = Size(size.height, size.height)
                )
                drawCircle(
                    Color.White,
                    radius = size.height / 2 * 0.8f,
                    center = Offset(hue1 / 360 * this.size.width, this.size.height / 2f),
                    style = Stroke(width = size.height / 2 * 0.4f)
                )
                drawCircle(
                    Color.Black.copy(0.7f),
                    radius = size.height / 2,
                    center = Offset(hue1 / 360 * this.size.width, this.size.height / 2f),
                    style = Stroke(width = 2f)
                )
            })
    }
}


@Composable
fun HueVerticalSlider(hue: Float, onHueChanged: (Float) -> Unit) {
    var hue1 by remember {
        mutableStateOf(hue)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .pointerInput(key1 = Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        hue1 = (offset.y / size.height * 360).coerceIn(0f, 360f)
                        onHueChanged(hue1)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    hue1 = (change.position.y / size.height * 360).coerceIn(0f, 360f)
                    onHueChanged(hue1)
                }
            }
            .padding(16.dp, 16.dp)
            .width(sliderHeight),
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Brush.verticalGradient(hueColors), RectangleShape),
            onDraw = {
                drawArc(
                    color = Color.Red, //left
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(0f, -size.width/2),
                    size = Size(size.width, size.width)
                )
                drawArc(
                    color = Color.Red, //right
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(0f, size.height - size.width / 2),
                    size = Size(size.width, size.width)
                )
                drawCircle(
                    Color.White,
                    radius = size.width / 2 * 0.8f,
                    center = Offset( this.size.width / 2f , hue1 / 360 * this.size.height,),
                    style = Stroke(width = size.width / 2 * 0.4f)
                )
                drawCircle(
                    Color.Black.copy(0.7f),
                    radius = size.width / 2,
                    center = Offset(this.size.width / 2f,hue1 / 360 * this.size.height, ),
                    style = Stroke(width = 2f)
                )
            })
    }
}
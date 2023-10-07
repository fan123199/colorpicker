package im.fdx.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import im.fdx.colorpicker.panel.ColorRectPanel
import im.fdx.colorpicker.panel.ColorWheel
import im.fdx.colorpicker.panel.toHSV


class ColorState() {
    var offset by mutableStateOf(Offset.Zero)
}


@Composable
fun ColorPickerRectHsvDialog(
    modifier: Modifier = Modifier,
    initColor: Color = Color.Green,
    dialogShape: Shape = RoundedCornerShape(5.dp),
    dialogBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    onConfirm: (Color) -> Unit = {}
) {
    var color by remember {
        mutableStateOf(initColor)
    }

    Dialog(onDismissRequest = {
        onConfirm(color)
    }) {

        Surface(
            modifier = modifier,
            color = dialogBackgroundColor,
            shape = dialogShape,
            tonalElevation = 2.dp
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                ColorPickerRectHsv(initColor = initColor) {
                    color = it
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { onConfirm(initColor) },
                    ) {
                        Text(stringResource(id = android.R.string.cancel))
                    }
                    TextButton(
                        onClick = { onConfirm(color) },
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                }

            }
        }

    }
}



@Composable
fun ColorPickerCircleHsvDialog(
    modifier: Modifier = Modifier,
    initColor: Color,
    dialogShape: Shape = RoundedCornerShape(5.dp),
    dialogBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    onConfirm: (Color) -> Unit = {}
) {
    var color by remember {
        mutableStateOf(initColor)
    }

    Dialog(onDismissRequest = {
        onConfirm(initColor)
    }) {

        Surface(
            modifier = modifier,
            color = dialogBackgroundColor,
            shape = dialogShape,
            tonalElevation = 2.dp
        ) {

            Column(horizontalAlignment = CenterHorizontally) {
                ColorPickerCircle(initColor = initColor) {
                    color = it
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { onConfirm(initColor) },
                    ) {
                        Text(stringResource(id = android.R.string.cancel))
                    }
                    TextButton(
                        onClick = { onConfirm(color) },
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                }

            }
        }

    }
}


//支持横屏dialog，避免裁剪
@Composable
fun ColorPickerCircle(
    modifier: Modifier = Modifier,
    initColor: Color,
    showAlpha: Boolean = false,
    onColorChanged: (Color) -> Unit = {}
) {
    val (h, s, v) = initColor.toHSV()

    var alpha by remember {
        mutableStateOf(initColor.alpha)
    }

    var hue by remember {
        mutableStateOf(h)
    }

    var saturation by remember {
        mutableStateOf(s)
    }

    var value by remember {
        mutableStateOf(v)
    }

    var color by remember {
        mutableStateOf(initColor)
    }

    BoxWithConstraints(modifier) {
        println("maxH $maxHeight,maxW $maxWidth,minH  $minHeight,minW  $minWidth")
        if(maxHeight > 450.dp) {
            Column {

                ColorWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .aspectRatio(1f),
                    alpha = alpha,
                    hue = h,
                    saturation = s,
                    value = value,
                    onColorChanged = { it, h, s ->
                        color = it
                        hue = h
                        saturation = s
                        onColorChanged(color)
                    },
                )

                ValueSlider(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onChanged = {
                        value = it
                    })

                if (showAlpha) {
                    AlphaSlider(color, alpha) {
                        alpha = it
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .background(color)
                        .heightIn(36.dp)
                        .width(100.dp),
                )

            }
        } else {
            Row(Modifier.height(IntrinsicSize.Max)) {

                ColorWheel(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .aspectRatio(1f, true),
                    alpha = alpha,
                    hue = h,
                    saturation = s,
                    value = value,
                    onColorChanged = { it, h, s ->
                        color = it
                        hue = h
                        saturation = s
                        onColorChanged(color)
                    },
                )



                ValueVerticalSlider(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onChanged = {
                        value = it
                    })

                if (showAlpha) {
                    AlphaVerticalSlider(color, alpha) {
                        alpha = it
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(CenterVertically)
                        .background(color)
                        .size(36.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    }

}


/**
 * Figma风格
 */
// todo 还没实现支持横屏dialog，可以copy下
@Composable
fun ColorPickerRectHsv(
    modifier: Modifier = Modifier,
    initColor: Color,
    showAlpha: Boolean = false,
    onColorChanged: (Color) -> Unit = {}
) {

    val (h, s, v) = initColor.toHSV()
    println("hsvhsvhsv,$h, $s, $v")

    var alpha by remember {
        mutableStateOf(initColor.alpha)
    }
    var hue by remember {
        mutableStateOf(h)
    }

    var saturation by remember {
        mutableStateOf(s)
    }

    var value by remember {
        mutableStateOf(v)
    }

    //目前的设计是带alpha的color!!
    var color by remember {
        mutableStateOf(initColor)
    }


    BoxWithConstraints(modifier) {
        if(maxHeight > 450.dp) {
            Column {

                ColorRectPanel(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .aspectRatio(1f),
                    alpha = alpha,
                    hue = hue,
                    saturation = s,
                    value = v,
                    onColorChanged = {
                        color = it
                        onColorChanged(color)
                    })

                HueSlider(hue) {
                    hue = it
                }

                if (showAlpha) {
                    AlphaSlider(color, alpha) {
                        alpha = it
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .background(color)
                        .heightIn(36.dp)
                        .width(100.dp),
                )

            }
        } else {

            Row(Modifier.height(IntrinsicSize.Max)) {

                ColorRectPanel(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .aspectRatio(1f, true),
                    alpha = alpha,
                    hue = hue,
                    saturation = s,
                    value = v,
                    onColorChanged = { it,->
                        color = it
                        onColorChanged(color)
                    },
                )



                HueVerticalSlider(hue) {
                    hue = it
                }

                if (showAlpha) {
                    AlphaVerticalSlider(color, alpha) {
                        alpha = it
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(CenterVertically)
                        .background(color)
                        .size(36.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    }

}


fun Color.toColorHexString(): String {
    return String.format("%08X", this.toArgb())
}


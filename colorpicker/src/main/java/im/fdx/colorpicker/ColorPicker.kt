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
                ColorPickerRectHsv(initColor) {
                    color = it
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { onConfirm(initColor) },
                    ) {
                        Text("CANCEL")
                    }
                    TextButton(
                        onClick = { onConfirm(color) },
                    ) {
                        Text("SELECT")
                    }
                }

            }
        }

    }
}


@Composable
fun ColorPickerCircleHsvDialog(
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
        onConfirm(initColor)
    }) {

        Surface(
            modifier = modifier,
            color = dialogBackgroundColor,
            shape = dialogShape,
            tonalElevation = 2.dp
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                ColorPickerCircle(initColor) {
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
fun ColorPickerCircle(
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

        if(showAlpha) {
            AlphaSlider(color, alpha) {
                alpha = it
            }
        }

        ValueSlider(
            hue = hue,
            saturation = saturation,
            value = value,
            onChanged = {
            value = it
        })

        Spacer(
            modifier = Modifier
                .align(CenterHorizontally)
                .background(color)
                .heightIn(36.dp).width(100.dp),
        )

    }

}


/**
 * Figma风格
 */
@Composable
fun ColorPickerRectHsv(
    initColor: Color = Color.Green,
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

    //目前的设计是带alpha的color!!
    var color by remember {
        mutableStateOf(initColor)
    }


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

        if(showAlpha) {
            AlphaSlider(color, alpha) {
                alpha = it
            }
        }

        Spacer(
            modifier = Modifier
                .align(CenterHorizontally)
                .background(color)
                .heightIn(36.dp).width(100.dp),
        )

    }

}


fun Color.toColorHexString(): String {
    return String.format("%08X", this.toArgb())
}


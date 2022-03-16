package com.mtabvuri.pomodoit.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

private val primaryMediumEmphasisColor = Color(0xFFF5F5BB)

@Composable
fun SelectionSwitch(
    toggleState: Boolean,
    modifier: Modifier = Modifier,
    onToggleStateChanged: (Boolean) -> Unit
) {
    Switch(
        checked = toggleState,
        onCheckedChange = onToggleStateChanged,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.primaryVariant,
            checkedTrackColor = primaryMediumEmphasisColor
        ),
        modifier = modifier
    )
}

@Composable
fun ClickableBoxWithText(
    text: String,
    modifier: Modifier = Modifier,
    onClickShowContent: @Composable () -> Unit
) {

    var showContent by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalRippleTheme provides object: RippleTheme {
            @Composable
            override fun defaultColor(): Color = MaterialTheme.colors.onPrimary

            @Composable
            override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
                Color.Black,
                lightTheme = !isSystemInDarkTheme()
            )

        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .background(primaryMediumEmphasisColor)
                .fillMaxHeight()
                .clickable { showContent = !showContent }
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.contentColorFor(primaryMediumEmphasisColor),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        onClickShowContent()
    }

}

@Composable
fun ClickableBoxWithTextSetting(
    settingText: String,
    modifier: Modifier = Modifier,
    marginHorizontal: Dp = 12.dp,
    marginVertical: Dp = 20.dp,
    boxText: String,
    onBoxClickShowContent: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small),
            constraintSet = constraintsForClickableBoxWithTextSetting(marginHorizontal, marginVertical)
        ) {
            Text(
                text = settingText,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.layoutId(Constraints.Text)
            )

            ClickableBoxWithText(
                text = boxText,
                onClickShowContent = onBoxClickShowContent,
                modifier = Modifier.layoutId(Constraints.Box)
            )
        }
    }
}

@Composable
fun SelectionSwitchSetting(
    settingText: String,
    modifier: Modifier = Modifier,
    marginHorizontal: Dp = 12.dp,
    marginVertical: Dp = 20.dp,
    toggleState: Boolean = false,
    onToggleStateChanged: (Boolean) -> Unit
) {
    BoxWithConstraints(modifier) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
            constraintSet = constraintsForSelectionSwitchSetting(marginHorizontal, marginVertical)
        ) {
            Text(
                text = settingText,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.layoutId(Constraints.Text)
            )

            SelectionSwitch(
                toggleState = toggleState,
                onToggleStateChanged = onToggleStateChanged,
                modifier = Modifier.layoutId(Constraints.Box)
            )
        }
    }

}


@Composable
fun NumberPickerDialog(
    range: IntRange,
    initialValue: Int,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onValueChanged: (Int) -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(modifier) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(100.dp),
                factory = { context ->
                    NumberPicker(context).apply {
                        maxValue = range.last
                        minValue = range.first
                        value = initialValue
                        setOnValueChangedListener { _, _, newValue ->
                            onValueChanged(newValue)
                        }
                    }
                }
            )

            TextButton(onClick = onDismissRequest) {
                Text("Close")
            }
        }

    }
}

private fun constraintsForSelectionSwitchSetting(marginHorizontal: Dp, marginVertical: Dp) = ConstraintSet{
    val text = createRefFor(Constraints.Text)
    val box = createRefFor(Constraints.Box)

    constrain(text) {
        start.linkTo(parent.start, marginHorizontal)
        centerVerticallyTo(box)
    }

    constrain(box) {
        top.linkTo(parent.top, marginVertical)
        end.linkTo(parent.end, marginHorizontal)
        bottom.linkTo(parent.bottom, marginVertical)
    }
}

private fun constraintsForClickableBoxWithTextSetting(marginHorizontal: Dp, marginVertical: Dp) = ConstraintSet {
    val text = createRefFor(Constraints.Text)
    val box = createRefFor(Constraints.Box)

    constrain(text) {
        top.linkTo(parent.top, marginVertical)
        start.linkTo(parent.start, marginHorizontal)
        bottom.linkTo(parent.bottom, marginVertical)
    }

    constrain(box) {
        end.linkTo(parent.end)
    }
}

@Preview
@Composable
fun SelectionSwitchPreview() {
    PomoDoItTheme {
        var toggleState by remember { mutableStateOf(false) }
        SelectionSwitch(toggleState) {
            toggleState = !toggleState
        }
    }
}

@Preview(heightDp = 60)
@Composable
fun ClickableBoxPreview() {
    PomoDoItTheme {
        ClickableBoxWithText("25min") {
            // Do nothing
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionSwitchSettingPreview() {
    PomoDoItTheme {
        var toggleState by remember { mutableStateOf(false) }
        SelectionSwitchSetting(
            settingText = "Vibration",
            toggleState = toggleState
        ) {
            toggleState = !toggleState
        }
    }
}

@Preview(showBackground = true, heightDp = 60)
@Composable
fun ClickableBoxWithTextSettingPreview() {
    PomoDoItTheme {
        ClickableBoxWithTextSetting(
            settingText = "Pomodoro time",
            boxText = "25min"
        ) {
            // Do nothing
        }
    }
}

@Preview(showBackground = true, heightDp = 60)
@Composable
fun NumberPickerDialogPreview() {
    PomoDoItTheme {
        NumberPickerDialog(
            range = 25..60,
            initialValue = 30,
            onValueChanged = {
                // Do nothing
            },
            onDismissRequest = {
                // Do nothing
            }
        )
    }
}
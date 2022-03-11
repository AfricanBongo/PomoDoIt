package com.mtabvuri.pomodoit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

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
            checkedTrackColor = Color(0xFFF5F5BB)
        ),
        modifier = modifier
    )
}

@Composable
fun ClickableBoxWithText(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ClickableBoxWithTextSetting() {
    TODO()
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
            modifier = Modifier.fillMaxWidth(),
            constraintSet = settingConstraints(marginHorizontal, marginVertical)
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

private fun settingConstraints(marginHorizontal: Dp, marginVertical: Dp) = ConstraintSet {
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

@Preview
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
fun SettingWithSelectionSwitchPreview() {
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
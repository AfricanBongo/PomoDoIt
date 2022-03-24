package com.mtabvuri.pomodoit.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.ui.selectedIcon
import com.mtabvuri.pomodoit.ui.theme.OnSurfaceMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme
import com.mtabvuri.pomodoit.ui.theme.PrimaryMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.ToggleSelection

@Composable
fun SoundSelection(
    soundName: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    playButtonEnabled: Boolean = true,
    onPlaySound: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = soundName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.width(24.dp))

        PlayButton(
            enabled = playButtonEnabled,
            onPlay = onPlaySound
        )

        Spacer(modifier = Modifier.width(24.dp))

        if (selected) {
            Icon(
                imageVector = selectedIcon.imageVector,
                contentDescription = stringResource(selectedIcon.contentDescription),
                tint = ToggleSelection
            )
        }

    }
}

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
            checkedTrackColor = PrimaryMediumEmphasis
        ),
        modifier = modifier
    )
}

/**
 * A composable with a clickable box that shows a dialog when clicked.
 * @param text The text in the center of the box.
 * @param dialogTitle Title of the dialog.
 * @param dialogBackgroundColor The background color for the dialog.
 * @param dialogTitleColor The color of the title of the dialog.
 * @param closeButtonTextColor The text color of the button that dismisses the dialog.
 * @param dialogContent Composable within the dialog, dialog utilises a [Column] layout.
 * so composables should written taking this into consideration.
 */
@Composable
fun ClickableBoxWithDialog(
    text: String,
    modifier: Modifier = Modifier,
    dialogTitle: String,
    dialogBackgroundColor: Color,
    dialogTitleColor: Color,
    closeButtonTextColor: Color,
    dialogContent: @Composable () -> Unit
) {

    var showContent by remember { mutableStateOf(false) }

    // Hide/show the dialog.
    val onDismissRequest = { showContent = !showContent }

    // Custom ripple for the box.
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
                .defaultMinSize(minWidth = 80.dp)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.small)
                .background(PrimaryMediumEmphasis)
                .clickable { onDismissRequest() }
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.contentColorFor(PrimaryMediumEmphasis),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (showContent) {
            Dialog(onDismissRequest = onDismissRequest) {
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(dialogBackgroundColor)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Title of the dialog
                    Text(
                        text = dialogTitle,
                        style = MaterialTheme.typography.h6,
                        color = dialogTitleColor
                    )

                    // Content
                    dialogContent()

                    // Button that dismisses the dialog.
                    SecondaryButton(
                        text = stringResource(R.string.close_btn),
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = closeButtonTextColor
                        )
                    )
                }
            }
        }
    }

}

/**
 * A composable with a clickable box that shows a dialog when clicked.
 * @param settingTitle Title of the setting displayed from the start margin.
 * @param boxText The text in the center of the box.
 * @param dialogContent Composable within the dialog, dialog utilises a [Column] layout,
 * so composables should written taking this into consideration.
 */
@Composable
fun ClickableBoxWithDialogSetting(
    settingTitle: String,
    modifier: Modifier = Modifier,
    marginHorizontal: Dp = 12.dp,
    marginVertical: Dp = 20.dp,
    boxText: String,
    dialogBackgroundColor: Color = MaterialTheme.colors.surface,
    dialogTitleColor: Color = OnSurfaceMediumEmphasis,
    closeButtonTextColor: Color = OnSurfaceMediumEmphasis,
    dialogContent: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small),
            constraintSet = constraintsForClickableBoxWithTextSetting(marginHorizontal, marginVertical)
        ) {
            Text(
                text = settingTitle,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.layoutId(Constraints.Text)
            )

            ClickableBoxWithDialog(
                text = boxText,
                dialogTitle = settingTitle,
                dialogBackgroundColor = dialogBackgroundColor,
                dialogTitleColor = dialogTitleColor,
                closeButtonTextColor = closeButtonTextColor,
                dialogContent = dialogContent,
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
fun SoundSelectionPreview() {
    PomoDoItTheme {
        SoundSelection(
            soundName = "Arcade") {
            // Do nothing
        }
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
        ClickableBoxWithDialogSetting(
            settingTitle = "Pomodoro time",
            boxText = "25min"
        ) {
            // Do nothing
        }
    }
}
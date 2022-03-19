package com.mtabvuri.pomodoit.ui.settings

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.data.preferences.*
import com.mtabvuri.pomodoit.ui.components.ClickableBoxWithDialogSetting
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

@Composable
fun SettingsScreen() {
    SettingsBody()
}

/**
 * Contains settings split into sections.
 * @param sectionSpacing Spacing between section items.
 */
@Composable
fun SettingsBody(
    modifier: Modifier = Modifier,
    userPreferencesViewModel: UserPreferencesViewModel = viewModel(
        factory = UserPreferencesViewModelFactory(
            UserPreferencesRepository(LocalContext.current)
        )
    ),
    sectionSpacing: Dp = 40.dp
) {

    val userPreferences by userPreferencesViewModel.userPreferences.collectAsState(initial = DEFAULT_USER_PREFERENCE)

    Column(
        verticalArrangement = Arrangement.spacedBy(sectionSpacing),
        modifier = modifier
    ) {
        SettingsSection(header = stringResource(R.string.time)) {
            TimeSetting(
                settingTitle = stringResource(R.string.pomodoro_time),
                timeInMin = userPreferences.pomodoroTime,
                lowestTime = PreferenceTime.POMODORO_TIME_LOWEST,
                highestTime = PreferenceTime.POMODORO_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updatePomodoroTime
            )
            TimeSetting(
                settingTitle = stringResource(R.string.short_break_time),
                timeInMin = userPreferences.shortBreakTime,
                lowestTime = PreferenceTime.SHORT_BREAK_TIME_LOWEST,
                highestTime = PreferenceTime.SHORT_BREAK_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updateShortBreakTime
            )
            TimeSetting(
                settingTitle = stringResource(R.string.long_break_time),
                timeInMin = userPreferences.longBreakTime,
                lowestTime = PreferenceTime.LONG_BREAK_TIME_LOWEST,
                highestTime = PreferenceTime.LONG_BREAK_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updateLongBreakTime
            )
        }

        SettingsSection(header = stringResource(R.string.notification)) {

        }

    }
}

@Composable
private fun TimeSetting(
    settingTitle: String,
    timeInMin: Int,
    lowestTime: PreferenceTime,
    highestTime: PreferenceTime,
    onTimeChanged: (Int) -> Unit ) {
    StandardSettingBox(
        settingTitle = settingTitle,
        boxText = "${timeInMin}min") {

        MinutePicker(
            range = lowestTime.timeInMin..highestTime.timeInMin,
            initialValue = timeInMin,
            onValueChanged = onTimeChanged,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        )
    }
}


/**
 * Minute picker used with a
 */
@Composable
fun MinutePicker(
    range: IntRange,
    initialValue: Int,
    modifier: Modifier = Modifier,
    onValueChanged: (Int) -> Unit
) {
    AndroidView(
        modifier = modifier,
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
}

@Composable
fun SoundPicker() {
    TODO()
}

@Composable
private fun StandardSettingBox(
    settingTitle: String,
    boxText: String,
    dialogContent: @Composable () -> Unit
) {
    ClickableBoxWithDialogSetting(
        settingTitle = settingTitle,
        boxText = boxText,
        dialogContent = dialogContent,
        modifier = Modifier
            .requiredHeight(64.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
    )
}

/**
 * A section containing a header and settings content.
 * @param headerSpacing Spacing between the header and the content.
 * @param contentSpacing Spacing of items in  [content].
 */
@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    header: String = "",
    headerStyle: TextStyle = MaterialTheme.typography.h6,
    headerSpacing: Dp = 12.dp,
    contentSpacing: Dp = 12.dp,
    content: @Composable () -> Unit
) {

    Column(modifier, verticalArrangement = Arrangement.spacedBy(headerSpacing)) {
        Text(
            text = header,
            style = headerStyle
        )

        Column(verticalArrangement = Arrangement.spacedBy(contentSpacing)) {
            content()
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFE7, widthDp = 360, heightDp = 720)
@Composable
fun SettingsBodyPreview() {
    PomoDoItTheme {
        SettingsBody()
    }
}

@Preview(showBackground = true, heightDp = 60)
@Composable
fun MinutePickerDialogPreview() {
    PomoDoItTheme {
        MinutePicker(
            range = 25..60,
            initialValue = 30,
            onValueChanged = {
                // Do nothing
            }
        )
    }
}

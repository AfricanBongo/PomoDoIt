package com.mtabvuri.pomodoit.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.data.preferences.*
import com.mtabvuri.pomodoit.ui.components.ClickableBoxWithTextSetting
import com.mtabvuri.pomodoit.ui.components.NumberPickerDialog
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

        NumberPickerDialog(
            range = lowestTime.timeInMin..highestTime.timeInMin,
            initialValue = timeInMin,
            onDismissRequest = { TODO("Rewrite clickablebox to have dialog.") },
            onValueChanged = onTimeChanged,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.surface)
                .padding(8.dp),
        )
    }
}

@Composable
private fun StandardSettingBox(
    settingTitle: String,
    boxText: String,
    onBoxClickShowContent: @Composable () -> Unit
) {
    ClickableBoxWithTextSetting(
        settingText = settingTitle,
        boxText = boxText,
        modifier = Modifier
            .requiredHeight(64.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
    ) {
        onBoxClickShowContent()
    }
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

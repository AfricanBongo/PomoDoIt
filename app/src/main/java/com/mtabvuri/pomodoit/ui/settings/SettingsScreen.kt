package com.mtabvuri.pomodoit.ui.settings

import android.content.Context
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.data.notification.Sound
import com.mtabvuri.pomodoit.data.notification.Sounds
import com.mtabvuri.pomodoit.data.preferences.*
import com.mtabvuri.pomodoit.ui.components.ClickableBoxWithDialogSetting
import com.mtabvuri.pomodoit.ui.components.SelectionSwitch
import com.mtabvuri.pomodoit.ui.components.SelectionSwitchSetting
import com.mtabvuri.pomodoit.ui.components.SoundSelection
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

        // Times
        SettingsSection(header = stringResource(R.string.time)) {

            // Pomodoro Time
            TimeSetting(
                settingTitle = stringResource(R.string.pomodoro_time),
                timeInMin = userPreferences.pomodoroTime,
                lowestTime = PreferenceTime.POMODORO_TIME_LOWEST,
                highestTime = PreferenceTime.POMODORO_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updatePomodoroTime
            )

            // Short break time
            TimeSetting(
                settingTitle = stringResource(R.string.short_break_time),
                timeInMin = userPreferences.shortBreakTime,
                lowestTime = PreferenceTime.SHORT_BREAK_TIME_LOWEST,
                highestTime = PreferenceTime.SHORT_BREAK_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updateShortBreakTime
            )

            // Long break time
            TimeSetting(
                settingTitle = stringResource(R.string.long_break_time),
                timeInMin = userPreferences.longBreakTime,
                lowestTime = PreferenceTime.LONG_BREAK_TIME_LOWEST,
                highestTime = PreferenceTime.LONG_BREAK_TIME_HIGHEST,
                onTimeChanged = userPreferencesViewModel::updateLongBreakTime
            )
        }

        // Notifications
        SettingsSection(header = stringResource(R.string.notification)) {

            // Pomodoro sound
            SoundSetting(
                settingTitle = stringResource(R.string.pomodoro_sound),
                currentSound = userPreferences.pomodoroSound,
                onSoundSelected = userPreferencesViewModel::updatePomodoroSound)

            // Break sound
            SoundSetting(
                settingTitle = stringResource(R.string.break_sound),
                currentSound = userPreferences.breakSound,
                onSoundSelected = userPreferencesViewModel::updateBreakSound)

            // Vibration
            SelectionSwitchSetting(
                settingText = stringResource(R.string.vibration),
                toggleState = userPreferences.vibration,
                onToggleStateChanged = userPreferencesViewModel::updateVibration,
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colors.surface)
            )
        }

    }
}

/*
Background for content in the dialog of a setting.
 */
private val dialogContentBackground: @Composable (Modifier) -> Modifier = { modifier ->
    modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colors.background)
        .padding(horizontal = 24.dp, vertical = 16.dp)
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
            modifier = dialogContentBackground(Modifier),
        )
    }
}


/**
 * Implements a [NumberPicker] to allow a user to select a period of time.
 */
@Composable
private fun MinutePicker(
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
private fun SoundSetting(
    settingTitle: String,
    currentSound: Sound,
    onSoundSelected: (Sound) -> Unit
) {
    StandardSettingBox(
        settingTitle = settingTitle,
        boxText = currentSound.toString()
    ) {
        SoundPicker(
            currentSound = currentSound,
            onSoundSelected = onSoundSelected,
            modifier = dialogContentBackground(Modifier).requiredHeight(300.dp)
        )
    }
}

/*
Select a sound from a list of sounds.
*/
@Composable
private fun SoundPicker(
    modifier: Modifier = Modifier,
    currentSound: Sound,
    sounds: List<Sound> = Sound.values().toList(),
    onSoundSelected: (Sound) -> Unit
) {

    var soundPlaying by remember{ mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sounds) { sound ->
            if (sound != Sound.NONE) {
                val context = LocalContext.current

                SoundSelection(
                    soundName = sound.toString(),
                    selected = sound == currentSound,
                    playButtonEnabled = !soundPlaying,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onSoundSelected(sound) }
                ) {

                    // Disables all other play buttons in the sound picker when a sound is playing.
                    soundPlaying = true

                    Sounds.playSound(context, sound) { soundPlaying = false }
                }
            }

        }
    }
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

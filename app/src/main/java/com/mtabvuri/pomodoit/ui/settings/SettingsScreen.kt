package com.mtabvuri.pomodoit.ui.settings

import android.content.Context
import android.media.AudioManager
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
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModel
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModelFactory
import com.mtabvuri.pomodoit.ui.components.ClickableBoxWithTextSetting
import com.mtabvuri.pomodoit.ui.components.SelectionSwitchSetting
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

@Composable
fun SettingsScreen(userPreferencesViewModel: UserPreferencesViewModel = viewModel(
    factory = UserPreferencesViewModelFactory(
        UserPreferencesRepository(LocalContext.current)
    )
)) {

}

/**
 * Contains settings split into sections.
 * @param sectionSpacing Spacing between section items.
 */
@Composable
fun SettingsBody(modifier: Modifier = Modifier, sectionSpacing: Dp = 40.dp) {
    Column(
        verticalArrangement = Arrangement.spacedBy(sectionSpacing),
        modifier = modifier
    ) {
        SettingsSection(header = stringResource(R.string.time)) {
//            ClickableBoxWithTextSetting(
//                settingText = stringResource(R.string.pomodoro_time),
//                boxText = "25min",
//                modifier = Modifier
//                    .requiredHeight(64.dp)
//                    .clip(MaterialTheme.shapes.small)
//                    .background(MaterialTheme.colors.surface)
//            ) {
//
//            }
        }

        SettingsSection(header = stringResource(R.string.notification)) {

//            ClickableBoxWithTextSetting(
//                settingText = stringResource(R.string.pomodoro_time),
//                boxText = "25min",
//                modifier = Modifier
//                    .requiredHeight(64.dp)
//                    .clip(MaterialTheme.shapes.small)
//                    .background(MaterialTheme.colors.surface)
//            ) {
//
//            }

//            val context = LocalContext.current
//            var toggleState by remember { mutableStateOf(false) }
//            SelectionSwitchSetting(
//                settingText = stringResource(R.string.vibration),
//                toggleState = toggleState,
//                modifier = Modifier
//                    .requiredHeight(64.dp)
//                    .clip(MaterialTheme.shapes.small)
//                    .background(MaterialTheme.colors.surface)
//            ) {
//                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK,1.0f)
//                toggleState = !toggleState
//
//            }
        }

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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFE7)
@Composable
fun SettingsBodyPreview() {
    PomoDoItTheme {
        SettingsBody(Modifier.padding(16.dp))
    }
}

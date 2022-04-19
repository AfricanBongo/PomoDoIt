package com.mtabvuri.pomodoit.ui.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.nav.PortraitLayout
import com.mtabvuri.pomodoit.ui.components.PrimaryButton
import com.mtabvuri.pomodoit.ui.settings.SettingsBody
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

@Composable
fun PreferencesScreen(onContinue: () -> Unit) {

    PortraitLayout {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .verticalScroll(state = rememberScrollState())
                .padding(top = 40.dp, end = 24.dp, bottom = 32.dp, start = 24.dp)
        ) {

            Text(
                text = stringResource(R.string.preferences_statement),
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colors.onBackground
            )

            Spacer(Modifier.height(48.dp))

            // All the settings for the app
            SettingsBody()

            Spacer(Modifier.height(60.dp))

            // Continue button
            PrimaryButton(
                text = stringResource(R.string.continue_btn),
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().requiredHeight(48.dp)
            )
        }
    }
}

@Preview(heightDp = 720, widthDp = 320, showBackground = true, backgroundColor = 0xFFFFFFE7)
@Composable
fun ScreenPreview() {
    PomoDoItTheme {
        PreferencesScreen {
            // Do nothing
        }
    }
}
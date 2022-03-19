package com.mtabvuri.pomodoit.ui.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModel
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModelFactory
import com.mtabvuri.pomodoit.nav.PortraitLayout
import com.mtabvuri.pomodoit.ui.settings.SettingsBody

@Composable
fun PreferencesScreen(userPreferencesViewModel: UserPreferencesViewModel = viewModel(
    factory = UserPreferencesViewModelFactory(
        UserPreferencesRepository(LocalContext.current)
    )
)) {

    PortraitLayout {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 20.dp, vertical = 32.dp)
                .verticalScroll(state = rememberScrollState())
        ) {

            Text(
                text = stringResource(R.string.preferences_statement),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colors.onBackground
            )

            Spacer(Modifier.height(48.dp))

            SettingsBody()
        }
    }


}
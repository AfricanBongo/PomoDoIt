package com.mtabvuri.pomodoit.ui.preferences

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModel
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesViewModelFactory
import com.mtabvuri.pomodoit.nav.PortraitLayout

@Composable
fun PreferencesScreen(userPreferencesViewModel: UserPreferencesViewModel = viewModel(
    factory = UserPreferencesViewModelFactory(
        UserPreferencesRepository(LocalContext.current)
    )
)) {

    PortraitLayout {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            Text(text = "Animals we are")
        }
    }


}

private fun preferencesConstraints() {
    TODO()
}
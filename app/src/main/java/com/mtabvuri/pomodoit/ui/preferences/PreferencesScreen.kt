package com.mtabvuri.pomodoit.ui.preferences

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.mtabvuri.pomodoit.nav.LockScreenOrientation

@Composable
fun PreferencesScreen() {

    // Show the screen in only portrait mode.
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        Text(text = "Animals we are")
    }
}

private fun preferencesConstraints() {
    TODO()
}
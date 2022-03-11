package com.mtabvuri.pomodoit.ui.preferences

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.mtabvuri.pomodoit.nav.PortraitLayout

@Composable
fun PreferencesScreen() {

    PortraitLayout {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            Text(text = "Animals we are")
        }
    }


}

private fun preferencesConstraints() {
    TODO()
}
package com.mtabvuri.pomodoit.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {

}

/*
A box that has different buttons for each stage of the pomodoro
i.e. pause and play pomodoro stage.
 */
@Composable
private fun ButtonBox(
    modifier: Modifier = Modifier,
    pomodoroState: PomodoroState
) {
    when (pomodoroState) {
        // TODO
    }
}

@Composable
private fun StartPomodoroButton() {

}
package com.mtabvuri.pomodoit.ui.home

/**
 * A state in the pomodoro session.
 * @param changeState Trigger function when we want to change the state.
 */
sealed class PomodoroState {
    object Default: PomodoroState()
    class Play(val onPause: () -> Unit) : PomodoroState()
    class Pause(val onResume: () -> Unit, val onStop: () -> Unit) : PomodoroState()
    class Completed(val completedPomodoros: Int, onStart: () -> Unit): PomodoroState()
}
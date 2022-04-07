package com.mtabvuri.pomodoit.ui.home

/**
 * A state in the pomodoro session.
 */
sealed class PomodoroState {
    class Running(val onWait: () -> Unit) : PomodoroState()
    class Waiting(val onRun: () -> Unit, val onStop: () -> Unit) : PomodoroState()
    class Default(val completedPomodoros: Int, val onStart: () -> Unit): PomodoroState()
}
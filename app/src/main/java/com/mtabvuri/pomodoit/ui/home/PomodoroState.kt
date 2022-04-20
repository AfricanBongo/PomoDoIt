package com.mtabvuri.pomodoit.ui.home

/**
 * A state in the pomodoro session.
 */
sealed class PomodoroState(val session: String) {
    class Pomodoro(val onPause: () -> Unit): PomodoroState("Pomodoro")
    class Pause(val onResume: () -> Unit, val onStop: () -> Unit): PomodoroState("Pomodoro")
    class ShortBreak(val onEndBreak: () -> Unit): PomodoroState("Short break")
    class LongBreak(val onEndBreak: () -> Unit): PomodoroState("Long break")
    class Waiting(val onStart: () -> Unit): PomodoroState("Waiting")
}
package com.mtabvuri.pomodoit.ui.home

/**
 * A state in the pomodoro session.
 */
sealed class PomodoroState(val session: String) {
    class Pomodoro(val onPause: () -> Unit): PomodoroState("Pomodoro")
    open class Pause(val onResume: () -> Unit, val onStop: () -> Unit): PomodoroState("Pomodoro")
    class ShortBreak(val onPause: () -> Unit): PomodoroState("Short break")
    class LongBreak(val onPause: () -> Unit): PomodoroState("Long break")
    class PomodoroWaiting(val onStart: () -> Unit): PomodoroState("Waiting")
    class BreakWaiting(breakState: PomodoroState, val onStart: () -> Unit):
        PomodoroState(
            if (breakState is ShortBreak) breakState.session else "Long break"
        )
}
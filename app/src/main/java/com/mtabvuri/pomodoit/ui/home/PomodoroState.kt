package com.mtabvuri.pomodoit.ui.home

/**
 * A state in the pomodoro session.
 */
enum class PomodoroState(val session: String) {
    POMODORO("Pomodoro"),
    SHORT_BREAK("Short break"),
    LONG_BREAK("Long break"),
    WAITING("Waiting");
}
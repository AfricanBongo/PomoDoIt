package com.mtabvuri.pomodoit.model.preferences

import com.mtabvuri.pomodoit.model.notification.Sound

const val PREFERENCES_NAME = "user_preferences"

enum class PreferenceTime(val timeInMin: Int) {
    POMODORO_TIME_DEFAULT(25),
    SHORT_BREAK_TIME_DEFAULT(5),
    LONG_BREAK_TIME_DEFAULT(20),
    POMODORO_TIME_LOWEST(20),
    SHORT_BREAK_TIME_LOWEST(1),
    LONG_BREAK_TIME_LOWEST(10),
    POMODORO_TIME_HIGHEST(60),
    SHORT_BREAK_TIME_HIGHEST(30),
    LONG_BREAK_TIME_HIGHEST(120)
}


data class UserPreferences(
    val pomodoroTime: Int,
    val shortBreakTime: Int,
    val longBreakTime: Int,
    val pomodoroSound: Sound,
    val breakSound: Sound,
    val vibration: Boolean
)

val DEFAULT_USER_PREFERENCE = UserPreferences(
    pomodoroTime = PreferenceTime.POMODORO_TIME_DEFAULT.timeInMin,
    shortBreakTime = PreferenceTime.SHORT_BREAK_TIME_DEFAULT.timeInMin,
    longBreakTime = PreferenceTime.LONG_BREAK_TIME_DEFAULT.timeInMin,
    pomodoroSound = Sound.NONE,
    breakSound = Sound.NONE,
    vibration = true
)
package com.mtabvuri.pomodoit.data.preferences

const val PREFERENCES_NAME = "user_preferences"

const val POMODORO_TIME_DEFAULT = 25
const val SHORT_BREAK_TIME_DEFAULT = 5
const val LONG_BREAK_TIME_DEFAULT = 20

const val POMODORO_TIME_LOWEST = 20
const val SHORT_BREAK_TIME_LOWEST = 1
const val LONG_BREAK_TIME_LOWEST = 10

const val POMODORO_TIME_HIGHEST = 60
const val SHORT_BREAK_TIME_HIGHEST = 30
const val LONG_BREAK_TIME_HIGHEST = 120

data class UserPreferences(
    val pomodoroTime: Int,
    val shortBreakTime: Int,
    val longBreakTime: Int
)
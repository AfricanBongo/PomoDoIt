package com.mtabvuri.pomodoit.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mtabvuri.pomodoit.data.preferences.PreferenceTime.*
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository.PreferenceKeys.LONG_BREAK_TIME_KEY
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository.PreferenceKeys.POMODORO_TIME_KEY
import com.mtabvuri.pomodoit.data.preferences.UserPreferencesRepository.PreferenceKeys.SHORT_BREAK_TIME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            mapUserPreferences(preference)
        }

    suspend fun updatePomodoroTime(pomodoroTime: Int) {
        context.dataStore.edit { preferences ->
            if (pomodoroTime in (POMODORO_TIME_LOWEST.timeInMin) until POMODORO_TIME_HIGHEST.timeInMin + 1)
                preferences[PreferenceKeys.POMODORO_TIME_KEY] = pomodoroTime
        }
    }

    suspend fun updateShortBreakTime(breakTime: Int) {
        context.dataStore.edit { preferences ->
            if (breakTime in (SHORT_BREAK_TIME_LOWEST.timeInMin) until SHORT_BREAK_TIME_HIGHEST.timeInMin + 1)
                preferences[PreferenceKeys.SHORT_BREAK_TIME_KEY] = breakTime
        }
    }

    suspend fun updateLongBreakTime(breakTime: Int) {
        context.dataStore.edit { preferences ->
            if (breakTime in (LONG_BREAK_TIME_LOWEST.timeInMin) until LONG_BREAK_TIME_HIGHEST.timeInMin + 1)
                preferences[PreferenceKeys.LONG_BREAK_TIME_KEY] = breakTime
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the pomodoro time.
        val pomodoroTime = preferences[POMODORO_TIME_KEY] ?: POMODORO_TIME_DEFAULT.timeInMin
        val shortBreakTime = preferences[SHORT_BREAK_TIME_KEY] ?: SHORT_BREAK_TIME_DEFAULT.timeInMin
        val longBreakTime = preferences[LONG_BREAK_TIME_KEY] ?: LONG_BREAK_TIME_DEFAULT.timeInMin
        return UserPreferences(pomodoroTime, shortBreakTime, longBreakTime)
    }

    private object PreferenceKeys {
        val POMODORO_TIME_KEY = intPreferencesKey("pomodoro_time")
        val SHORT_BREAK_TIME_KEY = intPreferencesKey("short_break_time")
        val LONG_BREAK_TIME_KEY = intPreferencesKey("long_break_time")
    }
}
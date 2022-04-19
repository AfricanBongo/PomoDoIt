package com.mtabvuri.pomodoit.model.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mtabvuri.pomodoit.model.notification.Sound
import com.mtabvuri.pomodoit.model.preferences.PreferenceTime.*
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.BREAK_SOUND_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.LONG_BREAK_TIME_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.ONBOARDING_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.POMODORO_SOUND_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.POMODORO_TIME_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.SHORT_BREAK_TIME_KEY
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository.PreferenceKeys.VIBRATION_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)
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
                preferences[POMODORO_TIME_KEY] = pomodoroTime
        }
    }

    suspend fun updateShortBreakTime(breakTime: Int) {
        context.dataStore.edit { preferences ->
            if (breakTime in (SHORT_BREAK_TIME_LOWEST.timeInMin) until SHORT_BREAK_TIME_HIGHEST.timeInMin + 1)
                preferences[SHORT_BREAK_TIME_KEY] = breakTime
        }
    }

    suspend fun updateLongBreakTime(breakTime: Int) {
        context.dataStore.edit { preferences ->
            if (breakTime in (LONG_BREAK_TIME_LOWEST.timeInMin) until LONG_BREAK_TIME_HIGHEST.timeInMin + 1)
                preferences[LONG_BREAK_TIME_KEY] = breakTime
        }
    }

    suspend fun updatePomodoroSound(sound: Sound) {
        context.dataStore.edit { preferences ->
            preferences[POMODORO_SOUND_KEY] = sound.name
        }
    }

    suspend fun updateBreakSound(sound: Sound) {
        context.dataStore.edit { preferences ->
            preferences[BREAK_SOUND_KEY] = sound.name
        }
    }

    suspend fun updateVibration(shouldVibrate: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRATION_KEY] = shouldVibrate
        }
    }

    suspend fun cancelOnboarding(cancelOnboarding: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = !cancelOnboarding
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {

        val noSound = Sound.NONE.name

        // Get the pomodoro time.
        val pomodoroTime = preferences[POMODORO_TIME_KEY] ?: POMODORO_TIME_DEFAULT.timeInMin
        val shortBreakTime = preferences[SHORT_BREAK_TIME_KEY] ?: SHORT_BREAK_TIME_DEFAULT.timeInMin
        val longBreakTime = preferences[LONG_BREAK_TIME_KEY] ?: LONG_BREAK_TIME_DEFAULT.timeInMin
        val pomodoroSound = Sound.valueOf(preferences[POMODORO_SOUND_KEY] ?: noSound)
        val breakSound = Sound.valueOf(preferences[BREAK_SOUND_KEY] ?: noSound)
        val vibration = preferences[VIBRATION_KEY] ?: true
        val onboard = preferences[ONBOARDING_KEY] ?: true

        return UserPreferences(
            pomodoroTime = pomodoroTime,
            shortBreakTime = shortBreakTime,
            longBreakTime = longBreakTime,
            pomodoroSound = pomodoroSound,
            breakSound = breakSound,
            vibration = vibration,
            onboard = onboard
        )
    }

    private object PreferenceKeys {
        val ONBOARDING_KEY = booleanPreferencesKey("onboarding")
        val POMODORO_TIME_KEY = intPreferencesKey("pomodoro_time")
        val SHORT_BREAK_TIME_KEY = intPreferencesKey("short_break_time")
        val LONG_BREAK_TIME_KEY = intPreferencesKey("long_break_time")
        val POMODORO_SOUND_KEY = stringPreferencesKey("pomodoro_sound")
        val BREAK_SOUND_KEY = stringPreferencesKey("break_sound")
        val VIBRATION_KEY = booleanPreferencesKey("vibration")
    }
}
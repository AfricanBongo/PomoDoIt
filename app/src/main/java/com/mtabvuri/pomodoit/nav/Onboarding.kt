package com.mtabvuri.pomodoit.nav

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

const val ONBOARDING_PREFERENCE = "onboarding_preferences"
val ONBOARDING_KEY = booleanPreferencesKey("onboarding")

class OnboardingViewModel(@SuppressLint("StaticFieldLeak") private val context: Context): ViewModel() {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(ONBOARDING_PREFERENCE)
    }

    val shouldOnboard: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            return@map preferences[ONBOARDING_KEY] ?: true
        }


    fun cancelOnboarding(cancelOnboarding: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[ONBOARDING_KEY] = !cancelOnboarding
            }
        }
    }
}



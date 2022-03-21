package com.mtabvuri.pomodoit.data.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mtabvuri.pomodoit.data.notification.Sound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val repository: UserPreferencesRepository): ViewModel() {
    val userPreferences: Flow<UserPreferences> = repository.userPreferencesFlow

    fun updatePomodoroTime(pomodoroTime: Int) {
        viewModelScope.launch {
            repository.updatePomodoroTime(pomodoroTime)
        }
    }

    fun updateShortBreakTime(breakTime: Int) {
        viewModelScope.launch {
            repository.updateShortBreakTime(breakTime)
        }
    }

    fun updateLongBreakTime(breakTime: Int) {
        viewModelScope.launch {
            repository.updateLongBreakTime(breakTime)
        }
    }

    fun updatePomodoroSound(sound: Sound) {
        viewModelScope.launch {
            repository.updatePomodoroSound(sound)
        }
    }

    fun updateBreakSound(sound: Sound) {
        viewModelScope.launch {
            repository.updateBreakSound(sound)
        }
    }

    fun updateVibration(shouldVibrate: Boolean) {
        viewModelScope.launch {
            repository.updateVibration(shouldVibrate)
        }
    }
}

class UserPreferencesViewModelFactory(private val repository: UserPreferencesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserPreferencesRepository::class.java).newInstance(repository)
    }

}
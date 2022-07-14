package com.mtabvuri.pomodoit.ui.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mtabvuri.pomodoit.model.notification.Sound
import com.mtabvuri.pomodoit.model.pomodoro.SessionCountDownTimer
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository
import com.mtabvuri.pomodoit.model.preferences.longBreakTimeInMillis
import com.mtabvuri.pomodoit.model.preferences.pomodoroTimeInMillis
import com.mtabvuri.pomodoit.model.preferences.shortBreakTimeInMillis
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val preferencesRepo: UserPreferencesRepository) : ViewModel() {

    // Whether or not the session is a break.
    private var onBreak = false

    // Pomodoro time variables.
    private var _timeInMillis by mutableStateOf(0L)
    private var _pomodoroTime by mutableStateOf(0L)
    private var _shortBreakTime by mutableStateOf(0L)
    private var _longBreakTime by mutableStateOf(0L)

    /******** Pomodoro states ***************/

    private val pomodoroWaitingState = PomodoroState.PomodoroWaiting {
        _pomodoroState = pomodoroRunningState
        startTimer()
    }
    private val pomodoroRunningState = PomodoroState.Pomodoro { pauseTimer() }
    private val pauseState = PomodoroState.Pause(
        onResume = { resumeTimer() },
        onStop = { stopTimer() }
    )
    private val shortBreakRunningState = PomodoroState.ShortBreak {
        pauseTimer()
    }
    private val shortBreakWaitingState = PomodoroState.BreakWaiting(breakState = shortBreakRunningState) {
        _pomodoroState = shortBreakRunningState
        _playSound = true
        startTimer()
    }

    /*** End ******/

    private val onTick = {
        _timeInMillis = _timer.timeInMillisUntilFinished
        if (!onBreak) {
            _runningSessionCompletion = (_timeInMillis / _pomodoroTime) * 1.0f
        }
    }

    private val onDone = {
        // Complete a pomodoro session if time ends and it was not a break.
        if (!onBreak) {
            _completedSessions++
            _runningSessionCompletion = 0f
        }

        // Whether or not to start break session.
        onBreak = !onBreak

        // Play the notification sound whenever a session ends.
        _playSound

        createFreshTimer()
    }

    private var _pomodoroState: PomodoroState by mutableStateOf(pomodoroWaitingState)

    /**
     * @see PomodoroState
     */
    val pomodoroState: PomodoroState
        get() = _pomodoroState

    /**
     * The time in milliseconds left until the timer stops.
     */
    val timeInMillis: Long
        get() = _timeInMillis

    // Session variables
    private var _completedSessions by mutableStateOf(0)
    private var _runningSessionCompletion by mutableStateOf(0f)

    /**
     * The number of completed pomodoro session.
     */
    val completedSessions: Int
        get() = _completedSessions

    /**
     * Percentage completion of the current pomodoro session from 0.0 to 1.0
     * Is equal to zero when no pomodoro session is currently running.
     */
    val runningSessionCompletion: Float
        get() = _runningSessionCompletion


    // Pomodoro sounds
    private var _pomodoroSound: Sound by mutableStateOf(Sound.NONE)
    private var _breakSound: Sound by mutableStateOf(Sound.NONE)
    private var _playSound by mutableStateOf(false)
    val pomodoroSound: Sound
        get() = _pomodoroSound
    val breakSound: Sound
        get() = _breakSound
    val playSound: Boolean
        get() = _playSound

    private var _timer: SessionCountDownTimer = SessionCountDownTimer(0)

    // Fetch the times from the user preferences and create a new timer if the timer has changed.
    init {
        viewModelScope.launch {
            preferencesRepo.userPreferencesFlow.collect { userPreferences ->
                _pomodoroTime = userPreferences.pomodoroTimeInMillis()
                _shortBreakTime = userPreferences.shortBreakTimeInMillis()
                _longBreakTime = userPreferences.longBreakTimeInMillis()
                _pomodoroSound = userPreferences.pomodoroSound
                _breakSound = userPreferences.breakSound
                createFreshTimer()
            }
        }
    }

    private fun createFreshTimer() {
        _timer = SessionCountDownTimer(
            millisInFuture = if (onBreak) _shortBreakTime else _pomodoroTime,
            onTick = onTick,
            onDone = onDone
        )

        _timeInMillis = _timer.timeInMillisUntilFinished
        _pomodoroState = if (onBreak) shortBreakWaitingState else pomodoroWaitingState
    }

    private fun startTimer() {
        _timer.start()
    }

    private fun stopTimer() {
        _timer.stop()
        createFreshTimer()
    }

    private fun pauseTimer() {
        _timer.pause()
        _pomodoroState = pauseState
    }

    private fun resumeTimer() {
        _timer.resume()
        _pomodoroState = if (onBreak) shortBreakRunningState else pomodoroRunningState
    }

    fun onSoundPlayed() {
        _playSound = false
    }
}

class HomeScreenViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(UserPreferencesRepository::class.java)
            .newInstance(UserPreferencesRepository(context))
    }
}

package com.mtabvuri.pomodoit.model.pomodoro

import android.os.CountDownTimer

/**
 * A session countdown timer for
 */
class SessionCountDownTimer(
    private val millisInFuture: Long,
    val onTick: () -> Unit = { /* Empty */ },
    val onDone: () -> Unit = { /* Empty */ }
) {

    // Time in milliseconds.
    private var _timeInMillisUntilFinished: Long = millisInFuture

    // CountDownTimer.
    private lateinit var timer: SessionTimer

    // Whether or not the timer is running.
    private var _isRunning = false

    /**
     * Time in milliseconds.
     */
    val timeInMillisUntilFinished: Long
        get() = _timeInMillisUntilFinished


    /**
     * Whether the timer is running or not.
     */
    val isRunning: Boolean
        get() = _isRunning

    /**
     * Start or restart the timer.
     * Only starts the timer if wasn't already running.
     */
    fun start() {
        if (!_isRunning) {
            timer = SessionTimer(millisInFuture)
            timer.start()
            _isRunning = true
        }
    }

    /**
     * Pause the timer.
     * Only pauses the timer if it is already running.
     */
    fun pause() {
        if (_isRunning) {
            timer.cancel()
            _isRunning = false
        }
    }

    /**
     * Resume the timer.
     * Only resumes the timer if it was paused
     */
    fun resume() {
        if (!_isRunning) {
            timer = SessionTimer(_timeInMillisUntilFinished) // Re-instantiate with time left.
            timer.start() // Timer object is re-instantiated so we just start it again
            _isRunning = true
        }
    }

    /**
     * Stops the timer completely.
     */
    fun stop() {
        timer.cancel()
        _isRunning = false
    }

    private inner class SessionTimer(millis: Long) : CountDownTimer(millis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _timeInMillisUntilFinished = millisUntilFinished
            onTick()
        }

        override fun onFinish() {
            _timeInMillisUntilFinished = 0
            onDone()
        }
    }
}
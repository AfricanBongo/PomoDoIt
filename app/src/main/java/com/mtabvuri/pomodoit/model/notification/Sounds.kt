package com.mtabvuri.pomodoit.model.notification

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.mtabvuri.pomodoit.R

/**
 * An enum class that represents a sound that can be played.
 * @param resourceId The [RawRes] of the sound file to be read.
 */
enum class Sound(@RawRes val resourceId: Int) {
    ARCADE(R.raw.arcade),
    BEEP_BEEP(R.raw.beep_beep),
    BELL(R.raw.bell),
    BELL_DRUM(R.raw.bell_drum),
    CHILDISH(R.raw.childish),
    DING_DONG(R.raw.ding_dong),
    DOREAMON(R.raw.ding_dong),
    DREAMLAND(R.raw.dreamland),
    METAL(R.raw.metal),
    PIANO(R.raw.piano),
    SCORE(R.raw.score),
    TANG(R.raw.tang),
    TIMER(R.raw.timer),
    NONE(0);

    override fun toString(): String =
        if (this.name.contains("_")) {
            name.replace("_", " ").lowercase().capitalize(Locale.current)
        } else {
            name.lowercase().capitalize(Locale.current)
        }
}

object Sounds {
    /**
     * Starts playing a sound when invoked.
     */
    fun playSound(context: Context, sound: Sound, onCompletion: () -> Unit) {
        if (sound != Sound.NONE) {
            MediaPlayer
                .create(context, sound.resourceId)
                .apply {
                    start()
                    setOnCompletionListener { onCompletion() }
                }
        }
    }
}
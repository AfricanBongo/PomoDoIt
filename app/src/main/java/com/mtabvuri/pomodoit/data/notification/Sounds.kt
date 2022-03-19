package com.mtabvuri.pomodoit.data.notification

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.mtabvuri.pomodoit.R

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
    TIMER(R.raw.timer);

    override fun toString(): String =
        if (this.name.contains("_")) {
            name.replace("_", " ").lowercase().capitalize(Locale.current)
        } else {
            name.lowercase().capitalize(Locale.current)
        }
}

object Sounds {
    fun playSound(context: Context, sound: Sound, onCompletion: () -> Unit) {
        MediaPlayer
            .create(context, sound.resourceId)
            .apply {
                start()
                this.setOnCompletionListener { onCompletion() }
            }
    }
}
package com.mtabvuri.pomodoit

import android.app.Application
import timber.log.Timber

class PomoDoItApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.Forest)
    }
}
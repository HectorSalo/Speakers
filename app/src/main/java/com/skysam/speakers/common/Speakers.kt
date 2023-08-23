package com.skysam.speakers.common

import android.app.Application
import android.content.Context

/**
 * Created by Hector Chirinos on 21/08/2023.
 */

class Speakers: Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    object Speakers {
        fun getContext(): Context = appContext
    }
}
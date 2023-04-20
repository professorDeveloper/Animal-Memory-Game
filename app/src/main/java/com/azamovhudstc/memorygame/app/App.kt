package com.azamovhudstc.memorygame.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.azamovhudstc.memorygame.data.shp.MySharedPreference


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MySharedPreference.init(this)
    }
}
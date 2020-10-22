package com.example.github.thesports

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *   Created by zhangziyi on 10/19/20 10:43
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }
}
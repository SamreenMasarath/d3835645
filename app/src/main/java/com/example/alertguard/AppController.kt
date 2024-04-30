package com.example.alertguard

import android.app.Application
import android.content.Context

class AppController: Application() {

    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}
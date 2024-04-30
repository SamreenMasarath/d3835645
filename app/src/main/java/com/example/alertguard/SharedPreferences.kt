package com.example.alertguard

import android.content.Context

object SharedPreferences {

    private val sharedPref = AppController.context.getSharedPreferences(
        AppController.context.resources.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    fun saveString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String) : String {
        return sharedPref.getString(key, "") ?: ""
    }

    fun saveInt(key: String, value: Int) {
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String) : Int {
        return sharedPref.getInt(key, 0)
    }

    fun saveBoolean(key: String, value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String) : Boolean {
        return sharedPref.getBoolean(key, false)
    }

}
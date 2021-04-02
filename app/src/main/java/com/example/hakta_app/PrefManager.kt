package com.example.hakta_app

import android.content.Context
import android.content.SharedPreferences

class PrefManager(val context: Context){

    companion object {
        private const val PREF_NAME = "preferences"
        private const val PRIVATE_MODE = 0
        private const val IS_FIRST_LAUNCH = "isFirstLaunch"
        private const val TOKEN = "token"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstTime).apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun setToken(token: String) {
        editor.putString(TOKEN, token).apply()
    }
}
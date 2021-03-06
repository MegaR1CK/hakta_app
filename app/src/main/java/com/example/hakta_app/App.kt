package com.example.hakta_app

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://wsk2019.mad.hakta.pro/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val MAIN_API: Api = retrofit.create(Api::class.java)

        var TOKEN: String? = null
    }
}
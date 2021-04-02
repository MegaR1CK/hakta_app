package com.example.hakta_app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.hakta_app.App
import com.example.hakta_app.PrefManager
import com.example.hakta_app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefManager = PrefManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!prefManager.isFirstTimeLaunch()) {
                if (prefManager.getToken() != null) {
                    App.TOKEN = prefManager.getToken()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                else startActivity(Intent(this, SignInActivity::class.java))
            }
            else {
                prefManager.setFirstTimeLaunch(false)
                startActivity(Intent(this, OnBoardActivity::class.java))
            }
        }, 2000)
    }
}
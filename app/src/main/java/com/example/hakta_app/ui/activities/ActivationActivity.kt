package com.example.hakta_app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hakta_app.R

class ActivationActivity : AppCompatActivity() {
    companion object {
        const val PHONE_KEY = "phone"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
    }
}
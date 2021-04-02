package com.example.hakta_app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hakta_app.R
import com.example.hakta_app.ui.fragments.MainFragment
import com.yandex.mapkit.MapKitFactory

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("f1599495-f157-4283-bdd6-841ba07894f5")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_home)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()
    }
}
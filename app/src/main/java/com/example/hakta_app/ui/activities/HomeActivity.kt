package com.example.hakta_app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hakta_app.App
import com.example.hakta_app.PrefManager
import com.example.hakta_app.R
import com.example.hakta_app.errorAlert
import com.example.hakta_app.ui.fragments.MainFragment
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()

        side_rail.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        App.MAIN_API.logout(App.TOKEN ?: "").enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                App.TOKEN = null
                val prefManager = PrefManager(this@HomeActivity)
                prefManager.deleteToken()
                Toast.makeText(this@HomeActivity, getString(R.string.sign_out_success),
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                t.message?.let { this@HomeActivity.errorAlert(it) }
            }
        })
    }
}
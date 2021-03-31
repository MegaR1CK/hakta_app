package com.example.hakta_app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hakta_app.App
import com.example.hakta_app.HomeActivity
import com.example.hakta_app.R
import com.example.hakta_app.models.LoginDataModel
import com.example.hakta_app.models.TokenModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    companion object {
        const val MAIL_KEY = "mail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val mail = intent.extras?.getString(MAIL_KEY)
        mail?.let { field_mail_sign_in.setText(mail) }


        btn_sign_in.setOnClickListener {
            val email = field_mail_sign_in.text.toString()
            val password = field_password_sign_in.text.toString()
            if (validate(email, password)) {
                App.MAIN_API.login(LoginDataModel(email, password)).enqueue(object : Callback<TokenModel> {
                    override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                        Toast.makeText(this@SignInActivity,
                            R.string.sing_in_success,
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                    }
                    override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                        t.message?.let { it1 -> App.errorAlert(this@SignInActivity, it1) }
                    }
                })
            }
        }

        btn_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun validate(email: String, password: String): Boolean {
        hideErrors()
        when {
            email.isBlank() -> box_mail_sign_in.error = getString(R.string.error_empty_field)
            password.isBlank() -> box_password_sign_in.error = getString(R.string.error_empty_field)
            else -> return true
        }
        return false
    }

    private fun hideErrors() {
        box_mail_sign_in.error = ""
        box_password_sign_in.error = ""
    }
}
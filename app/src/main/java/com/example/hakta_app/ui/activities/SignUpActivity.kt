package com.example.hakta_app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hakta_app.App
import com.example.hakta_app.R
import com.example.hakta_app.errorAlert
import com.example.hakta_app.models.PhoneModel
import com.example.hakta_app.models.RegisterDataModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_sign_up.setOnClickListener {
            val email = field_mail_sign_up.text.toString()
            val name = field_name_sign_up.text.toString()
            val password = field_password_sign_up.text.toString()
            val repPassword = field_password_rep_sign_up.text.toString()
            val code = field_code_sign_up.text.toString()
            val phone = field_phone_sign_up.text.toString()

            if (validate(email, name, password, repPassword, code, phone)) {
                App.MAIN_API.register(RegisterDataModel(email, name, password, phone))
                    .enqueue(object : Callback<Unit> {
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            t.message?.let { it1 -> this@SignUpActivity.errorAlert(it1) }
                        }
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            sendCode(code, phone, email)
                        }
                    })
            }
        }
    }

    private fun sendCode(code: String, phone: String, mail: String) {
        App.MAIN_API.sendCode(PhoneModel(code, phone)).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                t.message?.let { it1 -> this@SignUpActivity.errorAlert(it1) }
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                startActivity(Intent(this@SignUpActivity, ActivationActivity::class.java)
                    .putExtra(ActivationActivity.PHONE_KEY, phone)
                    .putExtra(ActivationActivity.CODE_KEY, code)
                    .putExtra(SignInActivity.MAIL_KEY, mail))
            }
        })
    }

    private fun validate(
        email: String,
        name: String,
        password: String,
        repPassword: String,
        code: String,
        phone: String
    ): Boolean {
        val mailRegex = Regex("[A-Za-z0-9_-]+@[a-z]+\\.[a-z]{2,4}")
        hideErrors()
        when {
            email.isBlank() ->
                box_mail_sign_up.error = getString(R.string.error_empty_field)
            name.isBlank() ->
                box_name_sign_up.error = getString(R.string.error_empty_field)
            code.isBlank() ->
                box_code_sign_up.error = getString(R.string.error_empty_field)
            phone.isBlank() ->
                box_phone_sign_up.error = getString(R.string.error_empty_field)
            password.isBlank() ->
                box_password_sign_up.error = getString(R.string.error_empty_field)
            repPassword.isBlank() ->
                box_password_rep_sign_up.error = getString(R.string.error_empty_field)
            !email.matches(mailRegex) ->
                box_mail_sign_up.error = getString(R.string.error_incorrect_email)
            password != repPassword -> {
                box_password_sign_up.error = getString(R.string.error_password_confirm)
                box_password_rep_sign_up.error = getString(R.string.error_password_confirm)
            }
            phone.length != 10 ->
                box_phone_sign_up.error = getString(R.string.error_incorrect_phone)
            else -> return true
        }
        return false
    }

    private fun hideErrors() {
        box_mail_sign_up.error = ""
        box_name_sign_up.error = ""
        box_code_sign_up.error = ""
        box_phone_sign_up.error = ""
        box_password_sign_up.error = ""
        box_password_rep_sign_up.error = ""
    }
}
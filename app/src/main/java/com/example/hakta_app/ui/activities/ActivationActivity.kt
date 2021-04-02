package com.example.hakta_app.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.example.hakta_app.App
import com.example.hakta_app.R
import com.example.hakta_app.errorAlert
import com.example.hakta_app.models.CodeModel
import com.example.hakta_app.models.PhoneModel
import kotlinx.android.synthetic.main.activity_activation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivationActivity : AppCompatActivity() {
    companion object {
        const val CODE_KEY = "code"
        const val PHONE_KEY = "phone"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        val code = intent.extras?.getString(CODE_KEY) ?: ""
        val phone = intent.extras?.getString(PHONE_KEY) ?: ""
        val email = intent.extras?.getString(SignInActivity.MAIL_KEY) ?: ""
        val fullPhone = "+${code + phone}"
        val phoneCodes = mapOf(
            Pair("+971666666666", "6666"),
            Pair("+971555555555", "5555")
        )

        activation_desc.text = String.format(Locale.getDefault(),
            getString(R.string.activation_message), fullPhone)

        field_act_code.doAfterTextChanged {
            if (it?.length == 4) {
                if (phoneCodes.containsKey(fullPhone) && phoneCodes[fullPhone] == it.toString()) {
                    App.MAIN_API.activate(CodeModel(it.toString())).enqueue(object : Callback<Unit> {
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            t.message?.let { it1 -> this@ActivationActivity.errorAlert(it1) }
                        }

                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            startActivity(Intent(this@ActivationActivity,
                                SignInActivity::class.java).putExtra(SignInActivity.MAIL_KEY, email))
                        }
                    })
                }
                else {
                    field_act_code.text?.clear()
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(VibrationEffect
                        .createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            }
        }

        val timer = object : CountDownTimer(300000, 1000) {
            override fun onFinish() {
                btn_resend_code.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                text_timer.text = String.format(Locale.getDefault(),
                    getString(R.string.activation_timer), millisUntilFinished / 1000)
            }
        }
        timer.start()

        btn_resend_code.setOnClickListener {
            App.MAIN_API.sendCode(PhoneModel(code, phone)).enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    t.message?.let { it1 -> this@ActivationActivity.errorAlert(it1) }
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    timer.start()
                    btn_resend_code.isEnabled = false
                }
            })
        }
    }
}
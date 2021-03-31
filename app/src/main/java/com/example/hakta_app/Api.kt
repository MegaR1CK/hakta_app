package com.example.hakta_app

import com.example.hakta_app.models.LoginDataModel
import com.example.hakta_app.models.PhoneModel
import com.example.hakta_app.models.RegisterDataModel
import com.example.hakta_app.models.TokenModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("user/login")
    fun login(@Body loginData: LoginDataModel): Call<TokenModel>

    @POST("users")
    fun register(@Body regData: RegisterDataModel): Call<Unit>

    @POST("user/smsCode")
    fun sendCode(@Body phone: PhoneModel): Call<Unit>
}
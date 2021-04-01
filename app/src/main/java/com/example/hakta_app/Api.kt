package com.example.hakta_app

import com.example.hakta_app.models.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("user/login")
    fun login(@Body loginData: LoginDataModel): Call<TokenModel>

    @POST("users")
    fun register(@Body regData: RegisterDataModel): Call<Unit>

    @POST("user/smsCode")
    fun sendCode(@Body phone: PhoneModel): Call<Unit>

    @PUT("user/activation")
    fun activate(@Body code: CodeModel): Call<Unit>

     @GET("quests/popular")
     fun getBestQuests(@Header("Token") token: String): Call<BestQuestsResponse>

    @GET("user/currentTask")
    fun getCurrentTask(@Header("Token") token: String): Call<CurrentTaskResponse>
}
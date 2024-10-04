package com.example.aplicacion

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.69:8080/"

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient() // Permite un manejo más flexible del JSON
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Obtiene la instancia de Retrofit
    val retrofitInstance: Retrofit
        get() = retrofit
}


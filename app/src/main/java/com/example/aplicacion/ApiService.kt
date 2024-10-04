package com.example.aplicacion

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("estudiante")
    fun obtenerTodos(): Call<List<Student>>

    @POST("estudiante")
    fun registrar(@Body student: Student): Call<String>

    @PUT("estudiante/{id}")
    fun modificar(@Path("id") id: Int, @Body student: Student): Call<Void>

    @DELETE("estudiante/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>
}

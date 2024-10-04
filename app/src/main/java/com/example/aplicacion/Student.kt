package com.example.aplicacion

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Student(
    val id: Int?,
    val nombre: String,
    val edad: Int,
    val grupo: String,
    @SerializedName("promedioGeneral") val promediogeneral: Double
) : Serializable

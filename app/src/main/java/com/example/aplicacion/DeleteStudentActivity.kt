package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.ApiClient.retrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteStudentActivity : AppCompatActivity() {
    private var estudianteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        estudianteId = intent.getIntExtra("estudianteId", -1)

        if (estudianteId != -1) {
            eliminarEstudiante(estudianteId)
        } else {
            Toast.makeText(this, "Error al obtener el ID del estudiante", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun eliminarEstudiante(id: Int) {
        val apiService = retrofitInstance!!.create(ApiService::class.java)
        val call = apiService.eliminar(id)
        call!!.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DeleteStudentActivity,
                        "Estudiante eliminado",
                        Toast.LENGTH_SHORT
                    ).show()
                    redirigirAMainActivity()
                } else {
                    Toast.makeText(
                        this@DeleteStudentActivity,
                        "Error al eliminar estudiante",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Toast.makeText(this@DeleteStudentActivity, "Error de red", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun redirigirAMainActivity() {
        val intent = Intent(this@DeleteStudentActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese aquí
    }
}



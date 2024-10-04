package com.example.aplicacion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudentActivity : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextGrupo: EditText
    private lateinit var editTextPromedio: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextGrupo = findViewById(R.id.editTextGrupo)
        editTextPromedio = findViewById(R.id.editTextPromedio)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // Listener para validar la edad mientras se escribe
        editTextEdad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    s.length == 3 -> Toast.makeText(
                        this@AddStudentActivity,
                        "Edad válida",
                        Toast.LENGTH_SHORT
                    ).show()

                    s.length >= 4 -> Toast.makeText(
                        this@AddStudentActivity,
                        "Error: La edad debe tener exactamente 2 cifras",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        btnRegistrar.setOnClickListener {
            agregarEstudiante()
        }
    }

    private fun agregarEstudiante() {
        val nombre = editTextNombre.text.toString()
        val edadStr = editTextEdad.text.toString()
        val grupo = editTextGrupo.text.toString()
        val promedioStr = editTextPromedio.text.toString()

        // Validar que la edad sea un número de 2 o 3 cifras
        if (edadStr.length !in 2..3) {
            Toast.makeText(this, "La edad debe tener entre 2 y 3 cifras", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull() ?: run {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar que el promedio tenga 2 cifras
        if (promedioStr.length > 2) {
            Toast.makeText(this, "El promedio debe tener como máximo 2 cifras", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val promedio = promedioStr.toDoubleOrNull()?.let {
            // Convertir a decimal con dos decimales
            String.format("%.2f", it).toDouble()
        } ?: 0.0

        val student = Student(null, nombre, edad, grupo, promedio)

        val apiService = ApiClient.retrofitInstance.create(ApiService::class.java)
        apiService.registrar(student).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Mostrar el JSON de la respuesta para depuración
                    val jsonResponse = response.body() ?: ""
                    Log.d("AgregarEstudianteActivity", "Respuesta del servidor: $jsonResponse")

                    Toast.makeText(
                        this@AddStudentActivity,
                        "Estudiante agregado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Regresar a MainActivity
                } else {
                    // Mostrar mensaje de error detallado
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("AgregarEstudianteActivity", "Error en la respuesta: $errorBody")
                    Toast.makeText(
                        this@AddStudentActivity,
                        "Error al agregar estudiante: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = t.message ?: "Error desconocido"
                Toast.makeText(
                    this@AddStudentActivity,
                    "Estudiante agregado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Regresar a MainActivity

                // Opcional: Registra el error para depuración
                Log.e("AgregarEstudianteActivity", "Error en la conexión", t)
            }
        })
    }
}

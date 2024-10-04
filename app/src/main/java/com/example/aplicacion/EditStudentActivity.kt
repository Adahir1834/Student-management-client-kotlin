package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.ApiClient.retrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStudentActivity : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextGrupo: EditText
    private lateinit var editTextPromedio: EditText
    private lateinit var buttonGuardar: Button
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextGrupo = findViewById(R.id.editTextGrupo)
        editTextPromedio = findViewById(R.id.editTextPromedio)
        buttonGuardar = findViewById(R.id.buttonGuardar)

        student = intent.getSerializableExtra("estudiante") as? Student

        student?.let {
            editTextNombre.setText(it.nombre)
            editTextEdad.setText(it.edad.toString())
            editTextGrupo.setText(it.grupo)
            editTextPromedio.setText(it.promediogeneral.toString())
        }

        buttonGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun guardarCambios() {
        val nombre = editTextNombre.text.toString().trim()
        val grupo = editTextGrupo.text.toString().trim()
        val promedioStr = editTextPromedio.text.toString().trim()

        if (nombre.isEmpty() || grupo.isEmpty() || promedioStr.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val edadStr = editTextEdad.text.toString().trim()
        val edad = edadStr.toIntOrNull() ?: run {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val promedio = promedioStr.toDoubleOrNull() ?: run {
            Toast.makeText(this, "El promedio debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedEstudiante = student?.copy(
            nombre = nombre,
            edad = edad,
            grupo = grupo,
            promediogeneral = promedio
        )

        updatedEstudiante?.let {
            val apiService = retrofitInstance?.create(ApiService::class.java)
            val call = apiService?.modificar(it.id!!, it)
            call?.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditStudentActivity,
                            "Estudiante modificado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent =
                            Intent(this@EditStudentActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditStudentActivity,
                            "Error al modificar estudiante",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@EditStudentActivity,
                        "Error de red: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "Error al cargar datos del estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}

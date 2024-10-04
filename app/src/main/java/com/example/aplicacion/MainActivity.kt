package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var listViewAlumnos: ListView
    private lateinit var btnAgregarAlumno: Button
    private lateinit var btnActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        listViewAlumnos = findViewById(R.id.listViewAlumnos)
        btnAgregarAlumno = findViewById(R.id.btnAgregarAlumno)
        btnActualizar = findViewById(R.id.btnConsultar)

        // Cargar estudiantes al iniciar
        cargarEstudiantes()

        // Configurar el botón de agregar alumno
        btnAgregarAlumno.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de actualizar
        btnActualizar.setOnClickListener {
            cargarEstudiantes()
        }

        // Listener para la lista
        listViewAlumnos.setOnItemClickListener { parent, view, position, id ->
            val studentSeleccionado = parent.getItemAtPosition(position) as Student
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("estudiante", studentSeleccionado)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEstudiantes() // Actualizar la lista al regresar a la actividad
    }

    private fun cargarEstudiantes() {
        val apiService = ApiClient.retrofitInstance?.create(ApiService::class.java)
        apiService?.obtenerTodos()?.enqueue(object : Callback<List<Student>> {
            override fun onResponse(
                call: Call<List<Student>>,
                response: Response<List<Student>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val estudiantes = response.body() ?: emptyList()
                    val adapter = EstudianteAdapter(this@MainActivity, estudiantes)
                    listViewAlumnos.adapter = adapter
                } else {
                    // Manejar la respuesta no exitosa
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                // Manejo de errores
            }
        })
    }


}

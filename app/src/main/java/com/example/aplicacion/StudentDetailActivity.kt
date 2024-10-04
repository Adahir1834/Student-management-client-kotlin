package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var textViewNombre: TextView
    private lateinit var textViewEdad: TextView
    private lateinit var textViewGrupo: TextView
    private lateinit var textViewPromedio: TextView
    private lateinit var buttonModificar: Button
    private lateinit var buttonEliminar: Button
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        // Inicialización de vistas
        textViewNombre = findViewById(R.id.textViewNombre)
        textViewEdad = findViewById(R.id.textViewEdad)
        textViewGrupo = findViewById(R.id.textViewGrupo)
        textViewPromedio = findViewById(R.id.textViewPromedio)
        buttonModificar = findViewById(R.id.buttonModificar)
        buttonEliminar = findViewById(R.id.buttonEliminar)

        // Recuperar el estudiante de la Intent
        student = intent.getSerializableExtra("estudiante") as? Student

        // Actualizar vistas con los datos del estudiante
        student?.let {
            textViewNombre.text = it.nombre
            textViewEdad.text = it.edad.toString()
            textViewGrupo.text = it.grupo
            textViewPromedio.text = it.promediogeneral.toString()
        }

        // Configuración de click listeners
        buttonEliminar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        buttonModificar.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("estudiante", student)
            startActivity(intent)
        }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este estudiante?")
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, DeleteStudentActivity::class.java)
                intent.putExtra(
                    "estudianteId",
                    student?.id
                ) // Puede ser nulo, asegúrate de manejarlo en la actividad de eliminación
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }


}

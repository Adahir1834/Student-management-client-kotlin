package com.example.aplicacion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EstudianteAdapter(context: Context?, students: List<Student?>?) :
    ArrayAdapter<Student?>(
        context!!, 0, students!!
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val estudiante = getItem(position)

        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.item_estudiante, parent, false)
        }

        val tvNombre = convertView!!.findViewById<TextView>(R.id.tvNombre)
        tvNombre.text = estudiante!!.nombre

        return convertView
    }
}

package com.example.tarea260125.adapter

import android.speech.RecognizerIntent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tarea260125.databinding.TiendaLayoutBinding
import com.example.tarea260125.model.Articulo

class ArticuloViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding = TiendaLayoutBinding.bind(v)
    fun render(item : Articulo , onBorrar: (Articulo) -> Unit, onEdit : (Articulo) -> Unit){
        binding.tvNombre.text = item.nombre
        binding.tvDescripcion.text = item.descripcion
        binding.tvPrecio.text = item.precio.toString()

        binding.btBorrar.setOnClickListener {
            onBorrar(item)
        }
        binding.btEditar.setOnClickListener {
            onEdit(item)
        }

    }
}
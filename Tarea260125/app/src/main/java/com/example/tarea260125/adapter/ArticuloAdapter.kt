package com.example.tarea260125.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tarea260125.R
import com.example.tarea260125.model.Articulo

class ArticuloAdapter(
    var lista : MutableList<Articulo>,
    private val onBorrar: (Articulo) -> Unit,
    private val onEdit: (Articulo) -> Unit
): RecyclerView.Adapter<ArticuloViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.tienda_layout, parent, false)
        return ArticuloViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.render(lista[position], onBorrar, onEdit)
    }

}
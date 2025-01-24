package com.example.tarea260125.provider

import com.example.tarea260125.model.Articulo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticuloProvider {
    private val database = FirebaseDatabase.getInstance().getReference("tienda")
    fun getDatos(datosTienda : (MutableList<Articulo>) -> Unit){
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listado = mutableListOf<Articulo>()
                for (item in snapshot.children) {
                    val valor = item.getValue(Articulo::class.java)
                    if (valor != null) {
                        listado.add(valor)
                    }
                }
                listado.sortBy { it.nombre }
                datosTienda(listado)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al leer el realtime: ${error.message}")
            }
        })
    }
}
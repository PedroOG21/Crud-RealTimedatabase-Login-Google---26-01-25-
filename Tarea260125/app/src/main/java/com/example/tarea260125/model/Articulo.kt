package com.example.tarea260125.model

import java.io.Serializable

data class Articulo(
    val nombre: String ="",
    val descripcion: String = "",
    val precio : Float = 0F
) : Serializable

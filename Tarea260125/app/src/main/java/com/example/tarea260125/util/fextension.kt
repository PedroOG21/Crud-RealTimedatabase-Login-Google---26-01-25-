package com.example.tarea260125.util

fun String.encodeNombre(): String {
    return this
        .replace("@", "_AT_")
        .replace(".", "_DOT_")
        .replace("#", "_HASH_")
        .replace("$", "_DOLLAR_")
        .replace("/", "_SLASH_")
}

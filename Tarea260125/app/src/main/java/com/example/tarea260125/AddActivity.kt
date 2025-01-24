package com.example.tarea260125

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tarea260125.databinding.ActivityAddBinding
import com.example.tarea260125.model.Articulo
import com.example.tarea260125.util.encodeNombre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddActivity : AppCompatActivity() {

    private var nombre = ""
    private var descripcion = ""
    private var precio = 0f
    private var editando = false
    private var itemArticulo = Articulo()

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerDatos()
        setListeners()
    }

    private fun setListeners() {
        binding.btCancelar.setOnClickListener {
            finish()
        }
        binding.btAdd.setOnClickListener {
            addItem()
        }
    }

    private fun addItem() {
        if(!camposCorrectos()) return
        //Datos correctos
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tienda")
        val item=Articulo(nombre, descripcion, precio)
        val nodo=nombre.encodeNombre()

        if (editando) {
            // Actualización directa en modo edición
            database.child(nodo).setValue(item).addOnSuccessListener {
                Toast.makeText(this, "Artículo actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar el artículo", Toast.LENGTH_SHORT).show()
            }
        } else {
            database.child(nodo).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(
                            this@AddActivity,
                            "El nombre ya está registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        database.child(nodo).setValue(item).addOnSuccessListener {
                            finish()
                        }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@AddActivity,
                                    "Error al guardar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    private fun recogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            itemArticulo = datos.getSerializable("ITEM") as Articulo
            editando = true
            binding.tvTitulo.text = "Editando"

            // Asignamos los datos del artículo a las variables
            nombre = itemArticulo.nombre
            descripcion = itemArticulo.descripcion
            precio = itemArticulo.precio

            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.isEnabled = false
        binding.etNombre.setText(nombre)
        binding.etDescripcion.setText(descripcion)
        binding.etPrecio.setText(precio.toString())
    }

    private fun camposCorrectos(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        precio = binding.etPrecio.text.toString().toFloatOrNull() ?: 0f

        if (nombre.length < 3){
            binding.etNombre.error = "Error, el nombre debe de tener mas de 3 carácteres."
            return false
        }

        if (descripcion.length < 10){
            binding.etDescripcion.error = "Error, la descripcion debe de tener mas de 10 carácteres."
            return false
        }

        if (precio < 0 || precio > 10000){
            binding.etPrecio.error = "Error, el precio no puede ser menor que 0 ni mayor que 10000."
            return false
        }

        return true
    }
}
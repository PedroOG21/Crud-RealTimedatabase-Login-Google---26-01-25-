package com.example.tarea260125

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarea260125.adapter.ArticuloAdapter
import com.example.tarea260125.databinding.ActivityPrincipalBinding
import com.example.tarea260125.model.Articulo
import com.example.tarea260125.provider.ArticuloProvider
import com.example.tarea260125.util.encodeNombre
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPrincipalBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference

    var adapter = ArticuloAdapter(mutableListOf<Articulo>(),{item -> borrarItem(item)}, {item -> editarItem(item)})

    private fun editarItem(item: Articulo) {
        val b = Bundle().apply {
            putSerializable("ITEM",item)
        }
        irActivityAdd(b)
    }

    private fun borrarItem(item: Articulo) {
        database.child(item.nombre.encodeNombre()).removeValue()
            .addOnSuccessListener {
                val position = adapter.lista.indexOf(item)
                if (position != 1){
                    adapter.lista.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Toast.makeText(this,"Articulo borrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error al borrar el articulo.", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding =ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth  = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tienda")
        setRecicler()
        setListerners()
        setMenuLateral()
    }

    private fun setMenuLateral() {
        binding.navegation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_logout->{
                    auth.signOut()
                    finish()
                    true
                }
                R.id.item_salir->{
                    finishAffinity()
                    true
                }
                R.id.item_borrar->{
                    confirmarBorrado()
                    true
                }
                else -> true
            }
        }
    }

    private fun borrarTodo() {
        database.removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"Tienda eliminada.",Toast.LENGTH_SHORT).show()
                recuperarDatosTienda()
            } else {
                Toast.makeText(this,"No se ha podido eliminar la tienda...",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun confirmarBorrado(){
        val builder= AlertDialog.Builder(this)
            .setTitle("¿Borrar los articulos?")
            .setMessage("¿Quieres borrar todos los articulos?")
            .setNegativeButton("CANCELAR"){
                    dialog,_->dialog.dismiss()
            }
            .setPositiveButton("ACEPTAR"){
                    _,_->
                borrarTodo()
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        recuperarDatosTienda()
    }

    private fun recuperarDatosTienda() {
        val articuloProvider= ArticuloProvider()
        articuloProvider.getDatos { todosLosRegistros->
            binding.imageView2.visibility=if(todosLosRegistros.isEmpty()) View.VISIBLE else View.INVISIBLE
            //los ordeno por el nombre
            val registrosOrdenados = todosLosRegistros.sortedBy { it.nombre }

            adapter.lista= registrosOrdenados.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListerners() {
        binding.floatingActionButton.setOnClickListener{
            irActivityAdd()
        }
    }

    private fun irActivityAdd(bundle: Bundle ?= null) {
        val i = Intent(this,AddActivity::class.java)
        if (bundle != null){
            i.putExtras(bundle)
        }
        startActivity(i)
    }

    private fun setRecicler() {
        val layoutManager= LinearLayoutManager(this)
        binding.reciclerTienda.layoutManager = layoutManager

        binding.reciclerTienda.adapter = adapter
        recuperarDatosTienda()
    }
}
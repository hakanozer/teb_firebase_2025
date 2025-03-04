package com.example.project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project.models.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class NoteManager : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    lateinit var n_TxtTittle: EditText
    lateinit var n_TxtDetail: EditText
    lateinit var n_BtnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_manager)

        n_TxtTittle = findViewById(R.id.n_TxtTittle)
        n_TxtDetail = findViewById(R.id.n_TxtDetail)
        n_BtnSave = findViewById(R.id.n_BtnSave)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        refreshData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        n_BtnSave.setOnClickListener {
            val tittle = n_TxtTittle.text.toString()
            val detail = n_TxtDetail.text.toString()

            // firebase save
            if (tittle.isEmpty() && detail.isEmpty()) {
                Toast.makeText(this, "Tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show()
            } else {
                val note = Note(tittle, detail)
                val uid = auth.currentUser!!.uid
                database.reference.child("notes").child(uid).push().setValue(note)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Notunuz kaydedildi!", Toast.LENGTH_SHORT).show()
                            refreshData()
                        } else {
                            Toast.makeText(this, "Notunuz kaydedilemedi!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }

    fun refreshData() {
        // firebase read
        val uid = auth.currentUser!!.uid
        database.reference.child("notes").child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result.value
                Log.d("data", result.toString())
            }
        }
    }


}
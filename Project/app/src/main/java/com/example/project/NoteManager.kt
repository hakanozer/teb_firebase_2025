package com.example.project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project.dapters.NoteAdapter
import com.example.project.models.Note
import com.example.project.models.NoteKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NoteManager : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val notes = mutableListOf<NoteKey>()
    private var selectNote: NoteKey? = null

    lateinit var n_listview: ListView
    lateinit var n_TxtTittle: EditText
    lateinit var n_TxtDetail: EditText
    lateinit var n_BtnSave: Button
    lateinit var n_BtnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_manager)

        n_TxtTittle = findViewById(R.id.n_TxtTittle)
        n_TxtDetail = findViewById(R.id.n_TxtDetail)
        n_BtnSave = findViewById(R.id.n_BtnSave)
        n_BtnUpdate = findViewById(R.id.n_BtnUpdate)
        n_listview = findViewById(R.id.n_listview)

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
                            n_TxtTittle.setText("")
                            n_TxtDetail.setText("")
                            n_TxtTittle.requestFocus()
                            refreshData()
                        } else {
                            Toast.makeText(this, "Notunuz kaydedilemedi!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        n_listview.setOnItemClickListener { parent, view, position, id ->
            val note = notes[position]
            selectNote = note
            n_TxtTittle.setText(note.value?.tittle)
            n_TxtDetail.setText(note.value?.detail)
        }

        n_BtnUpdate.setOnClickListener {
            if (selectNote == null) {
                Toast.makeText(this, "Not seçin!", Toast.LENGTH_SHORT).show()
            }else {
                val tittle = n_TxtTittle.text.toString()
                val detail = n_TxtDetail.text.toString()
                val uid = auth.currentUser!!.uid
                val note = Note(tittle, detail)
                database.reference.child("notes").child(uid).child(selectNote!!.key!!).setValue(note)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Notunuz güncellendi!", Toast.LENGTH_SHORT).show()
                            refreshData()
                        } else {
                            Toast.makeText(this, "Notunuz güncellenemedi!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }

        n_listview.setOnItemLongClickListener { parent, view, position, id ->
            val note = notes[position]
            val uid = auth.currentUser!!.uid
            database.reference.child("notes").child(uid).child(note.key!!).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Notunuz silindi!", Toast.LENGTH_SHORT).show()
                    refreshData()
                }
            }
            true
        }


    }

    fun refreshData() {
        // firebase read
        val uid = auth.currentUser!!.uid
        database.reference.child("notes").child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                notes.clear()
                task.result.children.forEach {
                    val itemNote = NoteKey()
                    itemNote.key = it.key
                    itemNote.value = it.getValue(Note::class.java)
                    notes.add(itemNote)
                }
                val adapter = NoteAdapter(this, notes)
                n_listview.adapter = adapter
                Log.d("item", notes.toString())
            }
        }
    }


}
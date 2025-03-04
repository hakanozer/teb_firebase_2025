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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ContactManager : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var c_name: EditText
    lateinit var c_email: EditText
    lateinit var c_phone: EditText
    lateinit var c_btnSave: Button
    lateinit var c_BtnUpdate: Button
    lateinit var c_listView: ListView

    private val notes = mutableListOf<NoteKey>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_manager)

        c_name = findViewById(R.id.c_name)
        c_email = findViewById(R.id.c_email)
        c_phone = findViewById(R.id.c_phone)
        c_btnSave = findViewById(R.id.c_btnSave)
        c_BtnUpdate = findViewById(R.id.c_BtnUpdate)
        c_listView = findViewById(R.id.c_listView)

        auth = FirebaseAuth.getInstance()
        readContact()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        c_btnSave.setOnClickListener {
            saveContact()
        }

        c_BtnUpdate.setOnClickListener {
            updateContact()
        }


    }

    fun saveContact() {
        val name = c_name.text.toString()
        val email = c_email.text.toString()
        val phone = c_phone.text.toString()

        val db = Firebase.firestore
        val user = auth.currentUser
        val uid = user?.uid

        val contact = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )
        if(  name.isEmpty() && email.isEmpty() && phone.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show()
            return
        }

        if (uid != null) {
            db.collection("users").document(uid).collection("contacts").add(contact)
                .addOnSuccessListener { task ->
                    readContact()
                    Toast.makeText(this, "Kişi ekleme başarılı: ${task.id}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Kişi ekleme hatası: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun readContact() {
        val db = Firebase.firestore
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            notes.clear()
            db.collection("users").document(uid).collection("contacts").get()
                .addOnSuccessListener { task ->
                    for (item in task) {
                        val itemNote = NoteKey()
                        itemNote.key = item.id
                        val note = Note()
                        note.tittle = item.data["name"].toString()
                        note.detail = item.data["email"].toString()
                        note.date = item.data["phone"].toString().toLong()
                        itemNote.value = note
                        notes.add(itemNote)
                    }
                    val adapter = NoteAdapter(this, notes)
                    c_listView.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    Log.w("item", "Error getting documents.", exception)
                }
        }
    }

    fun updateContact() {

    }


}
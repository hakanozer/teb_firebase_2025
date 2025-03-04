package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    lateinit var d_logout: Button
    lateinit var d_btnNoteManager: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()
        d_logout = findViewById(R.id.d_logout)
        d_btnNoteManager = findViewById(R.id.d_btnNoteManager)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // user info
        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            val email = auth.currentUser!!.email
            Log.d("user", "auth: " + uid + " " + email)
        }

        d_logout.setOnClickListener {
            auth.signOut()
            finish()
        }

        d_btnNoteManager.setOnClickListener {
            startActivity(Intent(this, NoteManager::class.java))
        }

    }
}
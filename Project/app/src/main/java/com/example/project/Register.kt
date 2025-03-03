package com.example.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var r_username: EditText
    lateinit var r_password: EditText
    lateinit var r_btnLogin: Button
    lateinit var r_btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        r_username = findViewById(R.id.r_username)
        r_password = findViewById(R.id.r_password)
        r_btnLogin = findViewById(R.id.r_btnLogin)
        r_btnRegister = findViewById(R.id.r_btnRegister)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        r_btnLogin.setOnClickListener {
            finish()
        }

        r_btnRegister.setOnClickListener {
            registerUser()
        }
    }

    fun registerUser() {
        val username = r_username.text.toString()
        val password = r_password.text.toString()

        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Kayıt Başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
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

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var f_email: EditText
    lateinit var f_btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        f_email = findViewById(R.id.f_email)
        f_btnSend = findViewById(R.id.f_btnSend)
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        f_btnSend.setOnClickListener {
            val email = f_email.text.toString()
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                val error = "E-posta adresinizi kontrol ediniz!"
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful && task.isComplete) {
                        Toast.makeText(this, "Şifre sıfırlama maili gönderildi.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Şifre sıfırlama maili gönderilemedi.", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
}
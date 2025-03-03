package com.example.project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    lateinit var l_username: EditText
    lateinit var l_password: EditText
    lateinit var l_btnLogin: Button
    lateinit var l_btnRegister: Button
    lateinit var l_btnforgotpassword: Button
    lateinit var l_gsmLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        l_username = findViewById(R.id.l_username)
        l_password = findViewById(R.id.l_password)
        l_btnLogin = findViewById(R.id.l_btnLogin)
        l_btnRegister = findViewById(R.id.l_btnRegister)
        l_btnforgotpassword = findViewById(R.id.l_btnforgotpassword)
        l_gsmLogin = findViewById(R.id.l_gsmLogin)

        sharedPreferences = getSharedPreferences("userPref", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        auth = FirebaseAuth.getInstance()
        val sharedUsername = sharedPreferences.getString("username", "")
        if (sharedUsername!!.isNotEmpty()) {
            l_username.setText(sharedUsername)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        l_btnLogin.setOnClickListener {
            loginUser()
        }

        l_btnRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        l_btnforgotpassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        l_gsmLogin.setOnClickListener {
            startActivity(Intent(this, GsmLogin::class.java))
        }
    }

    private fun loginUser() {
        val username = l_username.text.toString()
        val password = l_password.text.toString()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    editor.putString("username", username)
                    editor.apply()
                    Toast.makeText(this, "Giriş Başarılı!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Dashboard::class.java))
                } else {
                    Toast.makeText(this, "Giriş Başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
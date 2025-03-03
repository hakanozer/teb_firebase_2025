package com.example.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class GsmLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var g_gsm: EditText
    lateinit var g_code: EditText
    lateinit var g_sms: Button
    lateinit var g_valid: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gsm_login)

        g_gsm = findViewById(R.id.g_gsm)
        g_code = findViewById(R.id.g_code)
        g_sms = findViewById(R.id.g_sms)
        g_valid = findViewById(R.id.g_valid)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        g_sms.setOnClickListener {
            val gsm = g_gsm.text.toString()
            if (gsm.isEmpty()) {
                g_gsm.error = "Lütfen GSM numaranızı giriniz."
                return@setOnClickListener
            }

            val gmsOption = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+90$gsm")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        auth.signInWithCredential(p0)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@GsmLogin,
                                        "Login success",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(this@GsmLogin, "Login fail", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(this@GsmLogin, "Sms send Fail", Toast.LENGTH_SHORT).show()
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(gmsOption)

        }

    }
}
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
    private var verificationId: String? = null

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
            val phoneNumber = g_gsm.text.toString()
            if (phoneNumber.isNotEmpty()) {
                sendVerificationCode(phoneNumber)
            } else {
                Toast.makeText(this, "Telefon numarası giriniz", Toast.LENGTH_SHORT).show()
            }
        }

        g_valid.setOnClickListener {
            val code = g_code.text.toString()
            if (code.isNotEmpty()) {
                verificationId?.let { verifyCode(it, code) }
            } else {
                Toast.makeText(this, "Kod giriniz", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@GsmLogin, "Doğrulama başarısız: ${p0.message}", Toast.LENGTH_LONG).show()

                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(id, token)
                    verificationId = id
                    Toast.makeText(this@GsmLogin, "Kod gönderildi", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Giriş başarısız", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var btnGoto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnGoto = findViewById(R.id.btnGoto)
        auth = FirebaseAuth.getInstance()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // push notification token
        FirebaseMessaging.getInstance().token.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("gcm", "onCreate: $token")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // auth logout
        // auth.signOut()

        if (auth.currentUser != null) {
            Log.d("login", "onCreate: " + auth.currentUser!!.uid)
            startActivity(Intent(this, Dashboard::class.java))
        }else {
            startActivity(Intent(this, Login::class.java))
        }

        btnGoto.setOnClickListener {
            // Event start
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login Button Click")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "LoginButton")

            bundle.putString(FirebaseAnalytics.Param.METHOD, "Login Button Method")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

            startActivity(Intent(this, Login::class.java))
        }

        // catch
        try {
            val str = "10a"
            val num = str.toInt()
        }catch (ex: Exception) {
            Log.e("catch", "onCreate: $ex")
            FirebaseCrashlytics.getInstance().setUserId("123456")
            FirebaseCrashlytics.getInstance().recordException(ex)
        }

    }


}
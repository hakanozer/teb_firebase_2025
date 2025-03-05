package com.example.project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class Dashboard : AppCompatActivity() {

    lateinit var d_logout: Button
    lateinit var d_btnNoteManager: Button
    lateinit var d_btnContactManager: Button
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        auth = FirebaseAuth.getInstance()
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        d_logout = findViewById(R.id.d_logout)
        d_btnNoteManager = findViewById(R.id.d_btnNoteManager)
        d_btnContactManager = findViewById(R.id.d_btnContactManager)

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

        d_btnContactManager.setOnClickListener {
            startActivity(Intent(this, ContactManager::class.java))
        }

        // config
        //val configSettings = FirebaseRemoteConfigSettings.Builder()
          //  .setMinimumFetchIntervalInSeconds(3600)
            //.build()
        //firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        /*
        firebaseRemoteConfig.activate().addOnCompleteListener(this@Dashboard) { task ->
            if (task.isSuccessful) {
                val btnColor = firebaseRemoteConfig.getString("btnColor")
                Log.d("Updated", "Fetch and activate succeeded : $btnColor")
                d_logout.setBackgroundColor(Color.parseColor(btnColor))
            }else {
                Log.d("Updated", "Fetch failed")
            }
        }

         */

        firebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {
                Log.d("Updated", "Updated keys: " + configUpdate.updatedKeys);

                if (configUpdate.updatedKeys.contains("btnColor")) {
                    firebaseRemoteConfig.activate().addOnCompleteListener(this@Dashboard) { task ->
                        if (task.isSuccessful) {
                            val btnColor = firebaseRemoteConfig.getString("btnColor")
                            d_logout.setBackgroundColor(Color.parseColor(btnColor))
                            Log.d("Updated", "Fetch and activate succeeded : $btnColor")
                        }else {
                            Log.d("Updated", "Fetch failed")
                        }
                    }
                }

                if (configUpdate.updatedKeys.contains("managerBtnStatus")) {
                    firebaseRemoteConfig.activate().addOnCompleteListener(this@Dashboard) { task ->
                        if (task.isSuccessful) {
                            val managerBtnStatus = firebaseRemoteConfig.getBoolean("managerBtnStatus")
                            d_btnContactManager.setEnabled(managerBtnStatus)
                            Log.d("Updated", "Fetch and activate succeeded : $managerBtnStatus")
                        }else {
                            Log.d("Updated", "Fetch failed")
                        }
                    }
                }


            }

            override fun onError(error : FirebaseRemoteConfigException) {
                Log.w("Updated", "Config update error with code: " + error.code, error)
            }
        })




    }
}
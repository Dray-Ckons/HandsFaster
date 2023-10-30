package com.example.handsfaster

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonGame = findViewById<Button>(R.id.buttonGame)
        val buttonPreferences = findViewById<Button>(R.id.buttonPreferences)
        val buttonAbout = findViewById<Button>(R.id.buttonAbout)
        val buttonExit = findViewById<Button>(R.id.buttonExit)

        buttonGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonPreferences.setOnClickListener {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }

        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        buttonExit.setOnClickListener {
            finishAffinity()
        }
    }
}


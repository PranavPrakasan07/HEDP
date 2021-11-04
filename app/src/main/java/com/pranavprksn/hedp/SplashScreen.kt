package com.pranavprksn.hedp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val tapView = findViewById<TextView>(R.id.tap)
        val animView = findViewById<LottieAnimationView>(R.id.animationView)

        tapView.setOnClickListener{
            Toast.makeText(applicationContext, "Setting up!", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)
        }

        animView.setOnClickListener{
            Toast.makeText(applicationContext, "Setting up!", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)
        }
    }
}
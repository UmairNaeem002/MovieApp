package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed

class SPscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spscreen)
        Handler(Looper.getMainLooper()).postDelayed(5000){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
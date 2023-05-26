package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class FeaturedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured)

        findViewById<Button>(R.id.toggle).setOnClickListener {
            var image:ImageView = findViewById<ImageView>(R.id.imageView2)
            image.setImageResource(R.drawable.hev);
        }
    }
}
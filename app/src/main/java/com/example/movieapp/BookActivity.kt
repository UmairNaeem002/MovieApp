package com.example.movieapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.movieapp.utils.showToast

class BookActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        findViewById<Button>(R.id.btnBook).setOnClickListener {
            var mov = findViewById<EditText>(R.id.editTextText).text.toString().trim().uppercase()
            var dt = findViewById<EditText>(R.id.editTextDate).text.toString().trim()
            var tn = findViewById<EditText>(R.id.editTextNumber).text.toString().trim()
            if(mov.isNullOrEmpty() || dt.isNullOrEmpty() || tn.isNullOrEmpty()){
                showToast("Please fill all fields.")
            }
            else{
                showToast("Tickets booked")
                startActivity(Intent(this,HomeActivity::class.java))
            }


        }

    }
}
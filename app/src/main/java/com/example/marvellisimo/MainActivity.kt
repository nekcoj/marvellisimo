package com.example.marvellisimo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        helloText.setOnClickListener(){
            val singleCharacterView = Intent(this, SingleCharacterView::class.java)
            startActivity(singleCharacterView)
        }
    }
}
package com.example.marvellisimo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Character: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.character_activity)
        actionBar?.setDisplayHomeAsUpEnabled(true);
    }
}
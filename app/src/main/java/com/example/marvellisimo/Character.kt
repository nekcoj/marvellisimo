package com.example.marvellisimo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.character.*
import kotlinx.android.synthetic.main.character_view_row.*

class Character: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character)

        recylerview_character.layoutManager = LinearLayoutManager(this)
        //recylerview_character.adapter =
    }
}
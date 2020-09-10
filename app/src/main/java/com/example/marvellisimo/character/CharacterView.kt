package com.example.marvellisimo.character

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R

class CharacterView : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_character_view)
    }
}
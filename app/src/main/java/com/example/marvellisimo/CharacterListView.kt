package com.example.marvellisimo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.character_list_view.*

class CharacterListView : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_list_view)


//            Log.d("__","LOG FROM MAIN LOG")
        characterList_recyclerView.layoutManager = LinearLayoutManager(this)
        characterList_recyclerView.adapter = CharacterListAdapter()


    }
}
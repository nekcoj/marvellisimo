package com.example.marvellisimo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvellisimo.data.Service
import kotlinx.android.synthetic.main.activity_character_list_view.*

class CharacterListView : MainActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list_view)
        val adapter = CharacterListAdapter()
        characterList_recyclerView.adapter = adapter
        characterList_recyclerView.layoutManager = LinearLayoutManager(this)
        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Characters"
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        searchView_characters.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.Favorite -> {
                Service.FavoriteModeOnCharacter = !Service.FavoriteModeOnCharacter
                characterList_recyclerView.adapter = CharacterListAdapter()
                if (Service.FavoriteModeOnCharacter){
                    item.setIcon(android.R.drawable.btn_star_big_on)
                }else{
                    item.setIcon(android.R.drawable.btn_star_big_off)
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }
}
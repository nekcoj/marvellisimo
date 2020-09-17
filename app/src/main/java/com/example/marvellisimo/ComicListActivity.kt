package com.example.marvellisimo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvellisimo.data.Service
import kotlinx.android.synthetic.main.activity_comic_search.*


class ComicListActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)

        val adapter = ComicListAdapter()
        rv_comics.adapter = adapter
        rv_comics.layoutManager = LinearLayoutManager(this)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Comics"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        searchView_comics.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
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
        var favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                    onBackPressed()
                true
            }
            R.id.Favorite -> {
                Service.FavoriteModeOnComic =!Service.FavoriteModeOnComic
                rv_comics.adapter = ComicListAdapter()
                if (Service.FavoriteModeOnComic){
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


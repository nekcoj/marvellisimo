package com.example.marvellisimo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.data.Service.Companion.checkIfFavoriteToggled
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_comic_search.*


class ComicListActivity: MainActivity() {
    companion object {
        val COMIC = "comic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)

        val model:ComicListAdapter.ComicViewModel by viewModels()

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
        checkIfFavoriteToggled(favMenuItem, COMIC)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                    onBackPressed()
                true
            }
            R.id.Favorite -> {
                Service.FavoriteModeOnComic = !Service.FavoriteModeOnComic
                rv_comics.adapter = ComicListAdapter()
                checkIfFavoriteToggled(item, COMIC)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


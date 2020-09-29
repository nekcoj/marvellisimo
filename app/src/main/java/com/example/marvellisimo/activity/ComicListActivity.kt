package com.example.marvellisimo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.marvellisimo.MainActivity
import com.example.marvellisimo.marvel.MarvelRetrofit
import com.example.marvellisimo.model.Comic
import com.example.marvellisimo.R
import com.example.marvellisimo.data.RealmData
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.data.Service.Companion.FavoriteModeOnComic
import com.example.marvellisimo.data.Service.Companion.menu_
import com.example.marvellisimo.data.Service.Companion.checkIfFavoriteToggled
import com.example.marvellisimo.data.Service.Companion.toggleNavbarItemsIfAuth
import io.realm.*
import kotlinx.android.synthetic.main.activity_comic_search.*


class ComicListActivity: MainActivity() {
    companion object {
        val COMIC = "comic"
        lateinit var realmResults: RealmResults<Comic>
    }

    private lateinit var realm: Realm

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)
        realm = Realm.getDefaultInstance()

        realmResults = realm.where(Comic::class.java).findAllAsync()
        Service.subscribeToComicRealm(realmResults, rv_comics)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Comics"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        searchView_comics.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                MarvelRetrofit.getAllComics(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val searchResults = RealmData.searchComic(newText)
                    Service.subscribeToComicRealm(searchResults, rv_comics)
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        var favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        checkIfFavoriteToggled(favMenuItem, COMIC)
        menu_ = menu!!
        toggleNavbarItemsIfAuth(menu_)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.Favorite -> {
                FavoriteModeOnComic = !FavoriteModeOnComic
                realm = Realm.getDefaultInstance()

                realmResults = if (FavoriteModeOnComic){
                    realm.where(Comic::class.java)?.equalTo("favorite", true)?.findAll()!!
                } else {
                    realm.where(Comic::class.java).findAllAsync()
                }

                Service.subscribeToComicRealm(realmResults, rv_comics)
                checkIfFavoriteToggled(item, COMIC)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

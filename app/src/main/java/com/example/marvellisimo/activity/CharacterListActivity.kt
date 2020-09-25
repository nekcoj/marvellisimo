package com.example.marvellisimo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.marvellisimo.MainActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.data.RealmData
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.data.Service.Companion.FavoriteModeOnCharacter
import com.example.marvellisimo.data.Service.Companion.checkIfFavoriteToggled
import com.example.marvellisimo.data.Service.Companion.toggleNavbarItemsIfAuth
import com.example.marvellisimo.marvel.MarvelRetrofit
import com.example.marvellisimo.model.Character
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_character_list_view.*

class CharacterListView : MainActivity(){
    companion object{
        val CHARACTER = "character"
        lateinit var realmResults: RealmResults<Character>
    }
    private lateinit var realm: Realm

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list_view)
        realm = Realm.getDefaultInstance()

        realmResults = realm.where(Character::class.java).findAllAsync()
        Service.subscribeToCharacterRealm(realmResults, characterList_recyclerView)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Characters"
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        searchView_characters.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                MarvelRetrofit.getAllCharacters(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val searchResults = RealmData.searchCharacter(newText)
                    Service.subscribeToCharacterRealm(searchResults, characterList_recyclerView)
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        var favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        checkIfFavoriteToggled(favMenuItem, CHARACTER)
        Service._menu = menu!!
        toggleNavbarItemsIfAuth(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.Favorite -> {
                FavoriteModeOnCharacter = !FavoriteModeOnCharacter
                realm = Realm.getDefaultInstance()

                realmResults = if (FavoriteModeOnCharacter){
                    realm.where(Character::class.java)?.equalTo("favorite", true)?.findAll()!!
                } else {
                    realm.where(Character::class.java).findAllAsync()
                }
                Service.subscribeToCharacterRealm(realmResults, characterList_recyclerView)
                checkIfFavoriteToggled(item, CHARACTER)
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
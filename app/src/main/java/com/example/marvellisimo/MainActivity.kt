package com.example.marvellisimo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.activity.CharacterListView
import com.example.marvellisimo.activity.ComicListActivity
import com.example.marvellisimo.data.Service.Companion._menu
import com.example.marvellisimo.data.Service.Companion.toggleNavbarItemsIfAuth
import com.example.marvellisimo.firebase.FirebaseFunctions.Companion.logoutUser
import com.example.marvellisimo.marvel.MarvelRetrofit.getAllCharacters
import com.example.marvellisimo.marvel.MarvelRetrofit.getAllComics
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.model.Comic
import com.example.marvellisimo.user.ListAllUserActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_homepage.*

var realm: Realm? = null
private var runOnce: Boolean = false

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        if (!runOnce) {
            realm = Realm.getDefaultInstance()
            val realmResultsComics = realm?.where(Comic::class.java)?.findAllAsync()!!
            if (realmResultsComics.isEmpty()) {
                getAllComics()
            }
            val realmResultsCharacters = realm?.where(Character::class.java)?.findAllAsync()!!
            if (realmResultsCharacters.isEmpty()) {
                getAllCharacters()
            }
            runOnce = true
        }

        image_character.setOnClickListener {
            val intent = Intent(this, CharacterListView::class.java)
            startActivity(intent)
        }

        text_character.setOnClickListener {
            val intent = Intent(this, CharacterListView::class.java)
            startActivity(intent)
        }

        image_comics.setOnClickListener {
            val intent = Intent(this, ComicListActivity::class.java)
            startActivity(intent)
        }

        text_series.setOnClickListener {
            val intent = Intent(this, ComicListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        _menu = menu!!
        toggleNavbarItemsIfAuth(_menu)
        val favMenuItem: MenuItem? = menu.findItem(R.id.Favorite)
        favMenuItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Sign_in -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.Sign_in_text -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.Show_all_users -> {
                val intent = Intent(this, ListAllUserActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this,
                    "You clicked show contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.Log_Out -> {
                logoutUser()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "You clicked log out", Toast.LENGTH_SHORT).show()
            }
        }
            return true
    }

//    override fun onDestroy() {
//        logoutUser()
//        finish()
//        super.onDestroy()
//    }
}


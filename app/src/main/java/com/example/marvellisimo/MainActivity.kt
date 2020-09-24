package com.example.marvellisimo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.MarvelRetrofit.getAllCharacters
import com.example.marvellisimo.MarvelRetrofit.getAllComics
import com.example.marvellisimo.Model.Character
import com.example.marvellisimo.Model.Comic
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
        if(!runOnce) {
            realm = Realm.getDefaultInstance()
            val realmResultsComics = realm?.where(Comic::class.java)?.findAllAsync()!!
                if(realmResultsComics.isEmpty()){
                    getAllComics()
                }
            val realmResultsCharacters = realm?.where(Character::class.java)?.findAllAsync()!!
            if(realmResultsCharacters.isEmpty()){
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
        val favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        favMenuItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Sign_in -> {
                signIn()
                Toast.makeText(this, "You clicked Sign in", Toast.LENGTH_SHORT)
                .show()
            R.id.My_Contacts -> Toast.makeText(
                this,
                "You clicked show contacts",
                Toast.LENGTH_SHORT
            ).show()
            R.id.Add_Contact -> Toast.makeText(
                this,
                "You clicked add contacts",
                Toast.LENGTH_SHORT
            ).show()
            R.id.Log_Out -> Toast.makeText(this, "You clicked log out", Toast.LENGTH_SHORT)
                .show()
        }
        return true
    }
}


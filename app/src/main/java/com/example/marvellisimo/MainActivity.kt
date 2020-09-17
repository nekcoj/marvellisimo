package com.example.marvellisimo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.marvellisimo.Model.*
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_comic_search.*
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.character_list_view.*


object charList{
    var characters: MutableList<Character> = mutableListOf()

    fun getAllFavChar(characterList: MutableList<Character>):MutableList<Character>{
        var favChars : MutableList<Character> = mutableListOf()
        if (characterList.size > 0){
            for (char in characterList){
                if (char.favorite == true){
                    if (!favChars.contains(char)){
                        favChars.add(char)
                    }
                }
            }
        }
        return favChars
    }

    fun checkId(id:Int): Boolean{
        var isIdInList : Boolean = false
        characters.forEach{char ->
            if (char.id == id){
                isIdInList = true
            }
        }
        return isIdInList
        }
    }


object Limit{
    var comics: Int = 40
    var character: Int = 40
}

object FavoriteMode{
    var isOn = false
}

object Offset{
    var comics: Int = 0
}

var realm: Realm? = null

open class MainActivity : AppCompatActivity() {
    private var runOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        if(!runOnce) {
            MarvelRetrofit.getAllFavorite()
            MarvelRetrofit.getAllCharacters()
            MarvelRetrofit.getAllComics()
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


    override fun onStop() {
        super.onStop()
        saveData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Favorite -> {
                FavoriteMode.isOn = !FavoriteMode.isOn

            }
            R.id.Sign_in -> Toast.makeText(this, "You clicked Sign in", Toast.LENGTH_SHORT)
                .show()
            R.id.Characters -> Toast.makeText(
                this,
                "You clicked Search characters",
                Toast.LENGTH_SHORT
            ).show()
            R.id.Comics -> Toast.makeText(
                this,
                "You clicked Search comics",
                Toast.LENGTH_SHORT
            ).show()
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
            R.id.Save_Offline_Data -> {
                saveData()
                Toast.makeText(this, "Data saved for offline mode.", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun saveData(){
        saveComics()
        saveCharacters()
    }


    private fun saveComics() {
        ComicList.comics.forEach {comic ->
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(Comicbook().apply {
                        id = comic.id
                        title = comic.title
                        description = comic.description
                        thumbnail = ThumbnailDTO(comic.thumbnail.path, comic.thumbnail.extension)
                        urls = UrlDTO(comic.urls[0].type, comic.urls[0].url)
                    })
                }
            }
        }
    }

    private fun saveCharacters(){
        charList.characters.forEach {character ->
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(Character().apply {
                        id = character.id
                        title = character.name
                        description = character.description
                        thumbnail = ThumbnailDTO(character.thumbnail.path, character.thumbnail.extension)
                        urls = UrlDTO(character.urls[0].type, character.urls[0].url)
                    })
                }
            }
        }
    }

    companion object{
        fun saveFavorite(comicId: Int){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(FavouriteList().apply {
                        id = comicId
                    })
                }
            }
        }

        fun removeFromFavorite(id: Int){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm?.where<FavouriteList>()?.equalTo("id", id)?.findAll()
                    query?.deleteAllFromRealm()
                }
            }
        }

        fun getFavoriteIdList():MutableList<Int>{
            var favIdList : MutableList<Int> = mutableListOf()
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<FavouriteList>().findAll()
                    for (q in query){
                        if (!favIdList.contains(q)){
                            favIdList.add(q.id!!)
                        }
                    }
                }
            }
            return favIdList
        }
    }

}


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
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlin.math.log


object charList{
    var characters: MutableList<Character> = mutableListOf()
}

object Limit{
    var comics: Int = 10
}

object Offset{
    var comics: Int = 0
}

var realm: Realm? = null

class MainActivity : AppCompatActivity() {
    private var runOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        image_character.setOnClickListener {
            val intent = Intent(this, CharacterListView::class.java)
            startActivity(intent)
        }

        text_character.setOnClickListener {
            val intent = Intent(this, CharacterListView::class.java)
            startActivity(intent)
        }

        image_comics.setOnClickListener {
            saveData()
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Favorite -> Toast.makeText(
                this,
                "You clicked Favorite",
                Toast.LENGTH_SHORT
            ).show()
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
        }
        return true
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        if(!runOnce) {
            MarvelRetrofit.getAllCharacters()
            MarvelRetrofit.getAllComics()
            runOnce = true
        }
    }

    private fun saveData(){
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

        fun getFavoriteList():MutableList<Int>{
            var favIdList : MutableList<Int> = mutableListOf()
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<FavouriteList>().findAll()
                    for (q in query){
                        favIdList.add(q.id!!)
                    }
                }
            }
            return favIdList
        }
    }


}


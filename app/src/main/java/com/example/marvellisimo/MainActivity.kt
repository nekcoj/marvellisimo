package com.example.marvellisimo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_homepage.*


object charList{
    var characters: MutableList<Character> = mutableListOf()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        // var characterList = ArrayList<CharacterDataWrapper>()

        //actionBar?.setDisplayHomeAsUpEnabled(true);
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
            val intent = Intent(this, ComicListActivity::class.java)
            startActivity(intent)
        }
        text_series.setOnClickListener {
            val intent = Intent(this, ComicListActivity::class.java)
            startActivity(intent)
        }
    }*/
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
        super.startActivity(intent)
        MarvelRetrofit.marvelService.getAllCharacters(limit = 30, offset = 0)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    result.data.results.forEach { character ->
                        charList.characters?.add(character)
                        Log.d("__", "characters list size :" + charList.characters?.size.toString())
                        Log.d("__", character.name.toString())
                    }


                }
            }
    }
}


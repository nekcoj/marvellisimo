package com.example.marvellisimo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_homepage.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        var characterList = ArrayList<CharacterDataWrapper>()

        image_comics.setOnClickListener {
            val intent = Intent(this, ComicSearchActivity::class.java)
            startActivity(intent)
        }
        text_series.setOnClickListener {
            val intent = Intent(this, ComicSearchActivity::class.java)
            startActivity(intent)
        }

        image_character.setOnClickListener {

           MarvelRetrofit.marvelService.getAllCharacters(limit = 1, offset = 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result, err ->
                    characterList.add(result)
                    Log.d("Characterlist: ", characterList[0].toString())
                    if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                    else {
                        Log.d("___", "I got a CharacterDataWrapper $result")
                    }
                }
            val intent = Intent(this, ComicActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.Favorite -> Toast.makeText(this, "You clicked Favorite", Toast.LENGTH_SHORT).show()
            R.id.Sign_in -> Toast.makeText(this, "You clicked Sign in", Toast.LENGTH_SHORT).show()
            R.id.Characters -> Toast.makeText(this, "You clicked Search characters", Toast.LENGTH_SHORT).show()
            R.id.Comics -> Toast.makeText(this, "You clicked Search comics", Toast.LENGTH_SHORT).show()
            R.id.My_Contacts -> Toast.makeText(this, "You clicked show contacts", Toast.LENGTH_SHORT).show()
            R.id.Add_Contact -> Toast.makeText(this, "You clicked add contacts", Toast.LENGTH_SHORT).show()
            R.id.Log_Out -> Toast.makeText(this, "You clicked log out", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}

package com.example.marvellisimo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


object charList{
    var characters: MutableList<Character> = mutableListOf()
}

object Limit{
    var comics: Int = 10
}

object Offset{
    var comics: Int = 0
}


class MainActivity : AppCompatActivity() {

    private var runOnce: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        var cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    button_check_connection.setText("Connected to Network")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText()
                }
            })
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
            //MarvelRetrofit.getAllComics()
            Log.d("Image Comics: ", "clicked image!")
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
}


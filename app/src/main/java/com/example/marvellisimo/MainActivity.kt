package com.example.marvellisimo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_homepage.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

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

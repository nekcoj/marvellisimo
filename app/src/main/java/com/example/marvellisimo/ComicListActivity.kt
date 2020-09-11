package com.example.marvellisimo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_comic_search.*


class ComicListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)

        rv_comics.layoutManager = LinearLayoutManager(this)
        rv_comics.adapter = ComicListAdapter()

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Comics"
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


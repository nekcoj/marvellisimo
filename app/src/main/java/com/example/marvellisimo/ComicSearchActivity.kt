package com.example.marvellisimo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import java.util.Timer
import kotlin.concurrent.schedule

class ComicSearchActivity: AppCompatActivity() {
    var limit = 20
    var offset = 0
    var comicList = ArrayList<Comic>()
    lateinit var comics: ArrayList<ComicbookItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)
        comicList = drawComicList()
        Timer("SettingUp", false).schedule(2000) {
            Log.d("Hoppsan", "kerstin!")
            Log.d("comiclist:" , comicList.size.toString())
        }
        val rvComics = findViewById<View>(R.id.rvComics) as RecyclerView
        comics = ComicbookItem.createComicList(comicList.size, comicList)
        val adapter = ComicsAdapter(comics)
        rvComics.adapter = adapter
        rvComics.layoutManager = LinearLayoutManager(this)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun drawComicList(): ArrayList<Comic> {
        var comics = MarvelRetrofit.getComics(limit, offset)
        offset += limit
        return comics
    }
}

    class ComicbookItem(comic: Comic) {
        companion object {
            fun createComicList(numComics: Int, comicList: ArrayList<Comic>): ArrayList<ComicbookItem> {
                val comics = ArrayList<ComicbookItem>()
                for (i in 1..numComics) {
                    comics.add(ComicbookItem(comicList[i]))
                }
                return comics
            }
        }
    }

    class ComicsAdapter(private val mComics: ArrayList<ComicbookItem>) :
        RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {

        inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
            val nameTextView = itemView.findViewById<TextView>(R.id.comic_list_item_title)
            val coverImageView = itemView.findViewById<ImageView>(R.id.comic_list_cover_image)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ComicsAdapter.ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val comicsView = inflater.inflate(R.layout.activity_comic_list, parent, false)

            return ViewHolder(comicsView)
        }

        override fun onBindViewHolder(holder: ComicsAdapter.ViewHolder, position: Int) {
            val comic: ComicbookItem = mComics[position]

            val textView = holder.nameTextView
            textView.text = comic.toString()
            holder.coverImageView.setImageResource(R.mipmap.marvel_logo_small)
        }

        override fun getItemCount(): Int {
            return mComics.size
        }
    }
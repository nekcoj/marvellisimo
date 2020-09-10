package com.example.marvellisimo

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ComicSearchActivity: AppCompatActivity() {

    lateinit var comics: ArrayList<ComicbookItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_search)

        val rvComics = findViewById<View>(R.id.rvComics) as RecyclerView
        comics = ComicbookItem.createComicList(20)
        val adapter = ComicsAdapter(comics)
        rvComics.adapter = adapter
        rvComics.layoutManager = LinearLayoutManager(this)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
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

    class ComicbookItem(val name: String, val comicbook_cover_url: String) {

        companion object {
            private var lastComicId = 0
            fun createComicList(numComics: Int): ArrayList<ComicbookItem> {
                val comics = ArrayList<ComicbookItem>()
                for (i in 1..numComics) {
                    comics.add(ComicbookItem("Comic " + ++lastComicId, "google.com"))
                }
                return comics
            }
        }
    }

    class ComicsAdapter(private val mComics: List<ComicbookItem>) :
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
            textView.text = comic.name
            holder.coverImageView.setImageResource(R.mipmap.marvel_logo_small)
        }

        override fun getItemCount(): Int {
            return mComics.size
        }
    }
}
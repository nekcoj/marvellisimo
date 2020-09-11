package com.example.marvellisimo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comic_list.view.*

class ComicListAdapter: RecyclerView.Adapter<ComicViewHolder>() {

    override fun getItemCount(): Int {
        return ComicList.comics.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForComic = layoutInflater.inflate(R.layout.activity_comic_list, parent, false)
        return ComicViewHolder(cellForComic)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comicTitle = ComicList.comics[position].title
        val imgPath = renamePathHttps(ComicList.comics[position].thumbnail.path)
        val imgExt = ComicList.comics[position].thumbnail.extension
        val imgComplete = "$imgPath.$imgExt"
        Log.d("ImagePath: ", imgComplete)
        Picasso.get().load("$imgComplete").placeholder(R.mipmap.marvel_logo_small).into(holder.view.comic_list_cover_image);
        holder.view.comic_list_item_title.text = comicTitle

    }

    private fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}
class ComicViewHolder(val view: View): RecyclerView.ViewHolder(view){


}
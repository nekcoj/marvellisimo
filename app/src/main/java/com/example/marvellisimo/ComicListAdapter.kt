package com.example.marvellisimo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comic_list.view.*

class ComicListAdapter: RecyclerView.Adapter<ComicViewHolder>() {

    override fun getItemCount(): Int {
        return ComicList.comics.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForComic = layoutInflater.inflate(R.layout.activity_comic, parent, false)
        return ComicViewHolder(cellForComic)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comicTitle = ComicList.comics[position].title
        holder.view.comic_list_item_title.text = comicTitle

    }
}
class ComicViewHolder(val view: View): RecyclerView.ViewHolder(view){}
package com.example.marvellisimo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.CharacterListAdapter.Companion.renamePathHttps
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.data.RealmData
import com.example.marvellisimo.data.Service
import com.squareup.picasso.Picasso
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_comic_list.view.*

open class ComicListAdapter(comic: RealmResults<Comic>?): RecyclerView.Adapter<ComicViewHolder>() {

    var comicFilterList: RealmResults<Comic>? = if(Service.FavoriteModeOnComic){
        RealmData.favoriteComics()
    }else{
        comic
    }

    override fun getItemCount(): Int {
        return comicFilterList?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_comic_list, parent,false)
        return ComicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bindItems(comicFilterList?.get(position))

        holder.itemView.setOnClickListener(){
            val comic = comicFilterList?.get(position)
            val intent = Intent(holder.itemView.context, ComicActivity::class.java)
            intent.putExtra(ComicViewHolder.SELECTED_COMIC, comic)
            holder.itemView.context.startActivity(intent)
        }
    }
}

class ComicViewHolder(val view: View, var comic: Comic? = null): RecyclerView.ViewHolder(view){
    companion object{
        const val SELECTED_COMIC ="SELECTED_COMIC"
    }
    fun bindItems(comic: Comic?){
        val imgComplete = renamePathHttps(comic?.thumbnail?.path + "." + comic?.thumbnail?.extension)
        view.comic_list_item_title.text = comic?.title
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(view.comic_list_cover_image);

        if(comic?.favorite!!){
            view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_off)
        }

        view.comicFavIcon.setOnClickListener(){
            val isFav = comic.favorite
            if (isFav == true){
                view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_off)
                Service.changeFavoriteStatus(comic.id)
                RealmData.removeFromFavorite(comic.id!!)
                RealmData.saveComic(comic)
            }
            if (isFav == false){
                view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_on)

                Service.changeFavoriteStatus(comic.id)
                RealmData.saveFavorite(comic.id!!)
                RealmData.saveComic(comic)
            }
        }
    }
}
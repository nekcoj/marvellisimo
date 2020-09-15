package com.example.marvellisimo

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comic_list.view.*
import java.util.*

open class ComicListAdapter: RecyclerView.Adapter<ComicViewHolder>(), Filterable {
    var comicFilterList = mutableListOf<Comic>()

    init {
        comicFilterList = ComicList.comics
    }
    override fun getItemCount(): Int {
        return comicFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            @SuppressLint("CheckResult")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val comicSearch = constraint.toString()
                comicFilterList = if(comicSearch.isEmpty()){
                    ComicList.comics
                } else {
                    val resultList = mutableListOf<Comic>()
                    for(comic in ComicList.comics) {
                        if(comic.title.toLowerCase(Locale.ROOT).contains(comicSearch.toLowerCase(Locale.ROOT))){
                            resultList.add(comic)
                        }
                    }
                    if(resultList.isNotEmpty()){
                        resultList
                    } else {
                        MarvelRetrofit.marvelService.getAllComics(titleStartsWith = constraint.toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { result, err ->
                                if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                                else {
                                    Log.d("___", "I got a CharacterDataWrapper $result")
                                    result.data.results.forEach { comic ->
                                        if(!resultList.contains(comic)){
                                            resultList.add(comic)
                                            ComicList.comics.add(comic)
                                        }
                                    }
                                }
                            }
                        resultList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = comicFilterList
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                comicFilterList = results?.values as MutableList<Comic>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForComic = layoutInflater.inflate(R.layout.activity_comic_list, parent, false)
        return ComicViewHolder(cellForComic)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comicTitle = comicFilterList[position].title
        val imgPath = renamePathHttps(comicFilterList[position].thumbnail.path)
        val imgExt = comicFilterList[position].thumbnail.extension
        val imgComplete = "$imgPath.$imgExt"
        Picasso.get().load("$imgComplete").placeholder(R.mipmap.marvel_logo_small).into(holder.view.comic_list_cover_image);
        holder.view.comic_list_item_title.text = comicTitle
        holder?.comic = comicFilterList[position]
        comicFilterList[position].favorite = MainActivity.getFavoriteList().contains(comicFilterList[position].id)

        if (comicFilterList[position].favorite == true){
            holder?.view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_on)
        }else{
            holder?.view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_off)
        }

        holder?.view.comicFavIcon.setOnClickListener(){
            var isFav = comicFilterList[position].favorite
            if (isFav == true){
                holder?.view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_off)
                MainActivity.removeFromFavorite(comicFilterList[position].id)
                comicFilterList[position].favorite = false
            }
            if (isFav == false){
                holder?.view.comicFavIcon.setImageResource(android.R.drawable.btn_star_big_on)
                comicFilterList[position].favorite = true
                MainActivity.saveFavorite(comicFilterList[position].id)
            }
        }
    }

    private fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}
class ComicViewHolder(val view: View, var comic : Comic? = null): RecyclerView.ViewHolder(view){
    companion object{
        const val SELECTED_COMIC ="SELECTED_COMIC"
    }

    init {
        view.setOnClickListener(){
            val intent = Intent(view.context, ComicActivity::class.java)
            intent.putExtra(SELECTED_COMIC, comic)
            view.context.startActivity(intent)
        }
    }
}
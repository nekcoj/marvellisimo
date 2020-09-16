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
import com.example.marvellisimo.character.CharacterView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.character_view.view.*
import java.util.*

open class CharacterListAdapter : RecyclerView.Adapter<CharacterViewHolder>(), Filterable {
    var characterFilterList = mutableListOf<Character>()
    init {
        characterFilterList = if (FavoriteMode.isOn){
            charList.getAllFavChar(charList.characters)
        }else{
            charList.characters
        }

    }


    override fun getItemCount(): Int {
        return characterFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("CheckResult")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val characterSearch = constraint.toString()
                characterFilterList = if (characterSearch.isEmpty()) {
                    charList.characters
                } else {
                    val resultList = mutableListOf<Character>()
                    for (character in charList.characters) {
                        if (character.name.toLowerCase(Locale.ROOT).contains(characterSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(character)
                        }
                    }
                    if(resultList.size > 3) {
                        resultList
                     }
                    else {
                            MarvelRetrofit.marvelService.getAllCharacters(nameStartsWith = constraint.toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { result, err ->
                                    if (err?.message != null) Log.d(
                                        "__", "Error getAllCharacters " + err.message)
                                    else {
                                        Log.d("___", "I got a CharacterDataWrapper $result")
                                        result.data.results.forEach { character ->

                                            if (!resultList.contains(character)) {
                                                resultList.add(character)
                                                charList.characters.add(character)
                                            }
                                        }
                                    }
                                }
                            resultList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = characterFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                characterFilterList = results?.values as MutableList<Character>
                notifyDataSetChanged()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForCharacter = layoutInflater.inflate(R.layout.character_view, parent, false)
        return CharacterViewHolder(cellForCharacter)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val charName = characterFilterList[position].name
        val imgPath = renamePathHttps(characterFilterList[position].thumbnail.path)
        val imgExt = characterFilterList[position].thumbnail.extension
        val imgComplete = "$imgPath.$imgExt"
        Picasso.get().load("$imgComplete").placeholder(R.mipmap.marvel_logo_small).into(holder.view.imageView);
        holder?.view?.textView_characterName?.text = charName
        characterFilterList[position].favorite = MainActivity.getFavoriteList().contains(characterFilterList[position].id)

        if (characterFilterList[position].favorite == true){
            holder?.view.favIcon.setImageResource(android.R.drawable.btn_star_big_on)
        }else{
            holder?.view.favIcon.setImageResource(android.R.drawable.btn_star_big_off)
        }

        holder?.view.favIcon.setOnClickListener(){
            var isFav = characterFilterList[position].favorite
            if (isFav == true){
                holder?.view.favIcon.setImageResource(android.R.drawable.btn_star_big_off)
                MainActivity.removeFromFavorite(characterFilterList[position].id)
                characterFilterList[position].favorite = false
            }
            if (isFav == false){
                holder?.view.favIcon.setImageResource(android.R.drawable.btn_star_big_on)
                characterFilterList[position].favorite = true
                MainActivity.saveFavorite(characterFilterList[position].id)
            }
        }



        holder?.character = characterFilterList[position]
    }
    companion object{
        fun renamePathHttps(path: String): String {
            return path.replace("http", "https")
        }
        fun getImageLandscape(path: String): String {
            return path.replace(".jpg", "/portrait_xlarge.jpg")
        }
    }
}

class CharacterViewHolder(val view: View, var character: Character? = null): RecyclerView.ViewHolder(view){
    companion object{
        const val SELECTED_CHARACTER ="SELECTED_CHARACTER"
    }

    init {
        view.setOnClickListener(){
            val intent = Intent(view.context, CharacterView::class.java)
            intent.putExtra(SELECTED_CHARACTER,character)
            view.context.startActivity(intent)
        }
    }
}
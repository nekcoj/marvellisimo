package com.example.marvellisimo

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.character.CharacterView
import com.example.marvellisimo.data.Service
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_view.view.*
import java.util.*

open class CharacterListAdapter : RecyclerView.Adapter<CharacterViewHolder>(), Filterable {
    var characterFilterList = mutableListOf<Character>()

    init {
        characterFilterList = if (Service.FavoriteModeOnCharacter) {
            Service.getAllFavoriteCharacters(Service.characterList)
        } else {
            Service.characterList
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
                    Service.characterList
                } else {
                    val resultList = mutableListOf<Character>()
                    for (character in  Service.characterList) {
                        if (character.name?.toLowerCase(Locale.ROOT)?.contains(characterSearch.toLowerCase(Locale.ROOT))!!) {
                            resultList.add(character)
                        }
                    }
                    if (resultList.size > 3) {
                        resultList
                    } else {
                        MarvelRetrofit.getAllCharacters(constraint.toString())
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
        val cellForCharacter = layoutInflater.inflate(R.layout.activity_character_view, parent, false)
        return CharacterViewHolder(cellForCharacter)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val imgComplete = renamePathHttps(characterFilterList[position].thumbnail.path!! +"."+ characterFilterList[position].thumbnail.extension)
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(holder.view.imageView);
        holder.view.textView_characterName?.text = characterFilterList[position].name

        //characterFilterList[position].favorite = RealmData.getFavoriteIdList().contains(characterFilterList[position].id)

        if (characterFilterList[position].favorite == true) {
            holder.view.favIcon.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            holder.view.favIcon.setImageResource(android.R.drawable.btn_star_big_off)
        }

        holder.view.favIcon.setOnClickListener() {
            val isFav = characterFilterList[position].favorite
            if (isFav == true) {
                holder.view.favIcon.setImageResource(android.R.drawable.btn_star_big_off)
                RealmData.removeFromFavorite(characterFilterList[position].id!!)
                characterFilterList[position].favorite = false
            }
            if (isFav == false) {
                holder.view.favIcon.setImageResource(android.R.drawable.btn_star_big_on)
                characterFilterList[position].favorite = true
                RealmData.saveFavorite(characterFilterList[position].id!!)
            }
        }

        holder.character = characterFilterList[position]
    }

    companion object {
        fun renamePathHttps(path: String): String {
            return path.replace("http", "https")
        }

        fun getImageLandscape(path: String): String {
            return path.replace(".jpg", "/portrait_xlarge.jpg")
        }
    }
}

class CharacterViewHolder(val view: View, var character: Character? = null) :
    RecyclerView.ViewHolder(view) {
    companion object {
        const val SELECTED_CHARACTER = "SELECTED_CHARACTER"
    }

    init {
        view.setOnClickListener() {
            val intent = Intent(view.context, CharacterView::class.java)
            intent.putExtra(SELECTED_CHARACTER, character)
            view.context.startActivity(intent)
        }
    }
}
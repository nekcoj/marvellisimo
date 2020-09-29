package com.example.marvellisimo.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.R
import com.example.marvellisimo.activity.CharacterActivity
import com.example.marvellisimo.data.RealmData
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.data.Service.Companion.renamePathHttps
import com.squareup.picasso.Picasso
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_character_view.view.*
import com.example.marvellisimo.model.Character

open class CharacterListAdapter( character: RealmResults<Character>?) :
    RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {
    var characterList: RealmResults<Character>? = if(Service.FavoriteModeOnCharacter){
        RealmData.favoriteCharacters()
    }else{
        character
    }

    override fun getItemCount(): Int {
        return characterList?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val cellForCharacter = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_character_view, parent, false)
        return CharacterViewHolder(cellForCharacter)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindItems(characterList?.get(position))

        holder.itemView.setOnClickListener() {
            val character = characterList?.get(position)
            val intent = Intent(holder.itemView.context, CharacterActivity::class.java)
            intent.putExtra(CharacterViewHolder.SELECTED_CHARACTER, character)
            holder.itemView.context.startActivity(intent)
        }

    }

    class CharacterViewHolder(val view: View, var character: Character? = null) :
        RecyclerView.ViewHolder(view) {
        companion object {
            const val SELECTED_CHARACTER = "SELECTED_CHARACTER"
        }


        fun bindItems(character: Character?) {
            val imgComplete =
                renamePathHttps(character?.thumbnail?.path + "." + character?.thumbnail?.extension)
            view.characterName_adapter.text = character?.name
            Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small)
                .into(view.character_adapter_img);

            if (character?.favorite!!) {
                view.character_favIcon_adapter.setImageResource(R.mipmap.arc_icon_on2)
            } else {
                view.character_favIcon_adapter.setImageResource(R.mipmap.arc_icon_off)
            }

            view.character_favIcon_adapter.setOnClickListener() {
                val isFav = character.favorite
                if (isFav == true) {
                    view.character_favIcon_adapter.setImageResource(R.mipmap.arc_icon_off)
                    Service.changeCharacterFavoriteStatus(character.id)
                    RealmData.removeFromFavorite(character.id!!)
                    RealmData.saveCharacter(character)
                }
                if (isFav == false) {
                    view.character_favIcon_adapter.setImageResource(R.mipmap.arc_icon_on2)

                    Service.changeCharacterFavoriteStatus(character.id)
                    RealmData.saveFavorite(character.id!!)
                    RealmData.saveCharacter(character)
                }
            }
        }
    }
}
package com.example.marvellisimo

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.character.CharacterView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_view.view.*

class CharacterListAdapter : RecyclerView.Adapter<CustomViewHolder>() {


    override fun getItemCount(): Int {
        return charList.characters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForCharacter = layoutInflater.inflate(R.layout.character_view, parent, false)
        return CustomViewHolder(cellForCharacter)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val charName = charList.characters.get(position).name
        val imgPath = renamePathHttps(charList.characters[position].thumbnail.path)

        val imgExt = charList.characters[position].thumbnail.extension
        val imgComplete = "$imgPath.$imgExt"
        Log.d("ImagePath: " , imgComplete)
        Picasso.get().load("$imgComplete").placeholder(R.mipmap.marvel_logo_small).into(holder.view.imageView);
        holder?.view?.textView_characterName?.text = charName.toString()
    }

    private fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init {
        view.setOnClickListener(){
            println("Test!!!")
            val intent = Intent(view.context, CharacterView::class.java)
            view.context.startActivity(intent)
        }
    }
}
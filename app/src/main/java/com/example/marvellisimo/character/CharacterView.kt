package com.example.marvellisimo.character

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.Character
import com.example.marvellisimo.CharacterListAdapter
import com.example.marvellisimo.CustomViewHolder
import com.example.marvellisimo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_character_view.*


class CharacterView : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_character_view)
        val selectedCharacter = intent.getSerializableExtra(CustomViewHolder.SELECTED_CHARACTER) as? Character

        character_name.text = selectedCharacter?.name.toString()
        Picasso.get().load(CharacterListAdapter.renamePathHttps(selectedCharacter?.thumbnail?.path.toString()) + "." + selectedCharacter?.thumbnail?.extension).into(
            character_img
        )
        character_description.text = selectedCharacter?.description.toString()
        link.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a href='http://www.google.com'> Google </a>"
        link.text = Html.fromHtml(text)
    }
}
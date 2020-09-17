package com.example.marvellisimo.character

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.Character
import com.example.marvellisimo.CharacterListAdapter
import com.example.marvellisimo.CharacterViewHolder
import com.example.marvellisimo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_single_character_view.*


class CharacterView : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_character_view)
        val selectedCharacter = intent.getSerializableExtra(CharacterViewHolder.SELECTED_CHARACTER) as? Character



        character_name.text = selectedCharacter?.name.toString()
        Picasso.get().load(CharacterListAdapter.renamePathHttps(selectedCharacter?.thumbnail?.path.toString()) + "." + selectedCharacter?.thumbnail?.extension).into(character_img)
        character_description.text = selectedCharacter?.description.toString()
        link_character.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a color:#e62429; href='${selectedCharacter?.urls?.get(0)?.url}'> Want to know more ? </a>"
        link_character.text = Html.fromHtml(text)

    }
}


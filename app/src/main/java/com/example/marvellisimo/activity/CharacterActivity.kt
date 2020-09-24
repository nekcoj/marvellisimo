package com.example.marvellisimo.activity

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.adapter.CharacterListAdapter.CharacterViewHolder.Companion.SELECTED_CHARACTER
import com.example.marvellisimo.data.Service.Companion.renamePathHttps
import com.example.marvellisimo.model.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_single_character_view.*


class CharacterActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_character_view)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val selectedCharacter = intent.getParcelableExtra(SELECTED_CHARACTER) as? Character
        character_name.text = selectedCharacter?.name
        Picasso.get().load(renamePathHttps(selectedCharacter?.thumbnail?.path!!) + "." + selectedCharacter.thumbnail?.extension).into(character_img)
        character_description.text = if (!selectedCharacter.description.equals("")) selectedCharacter.description else "No description available"
        link_character.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a color:#e62429; href='${selectedCharacter?.urls?.url}'> Want to know more ? </a>"
        link_character.text = Html.fromHtml(text)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


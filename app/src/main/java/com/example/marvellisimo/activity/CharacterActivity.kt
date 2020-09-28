package com.example.marvellisimo.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.MainActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.SignInActivity
import com.example.marvellisimo.adapter.CharacterListAdapter.CharacterViewHolder.Companion.SELECTED_CHARACTER
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.data.Service.Companion.renamePathHttps
import com.example.marvellisimo.firebase.FirebaseFunctions
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.user.ListAllUserActivity
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        Service._menu = menu!!
        Service.toggleNavbarItemsIfAuth(Service._menu)
        val favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        favMenuItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.Sign_in -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.Sign_in_text -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.Show_all_users -> {
                val intent = Intent(this, ListAllUserActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this,
                    "You clicked show contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.Log_Out -> {
                FirebaseFunctions.logoutUser()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "You clicked log out", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}


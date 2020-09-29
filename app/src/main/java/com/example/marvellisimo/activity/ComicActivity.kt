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
import com.example.marvellisimo.data.Service
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comic.*
import com.example.marvellisimo.model.Comic
import com.example.marvellisimo.R
import com.example.marvellisimo.SignInActivity
import com.example.marvellisimo.adapter.ComicViewHolder
import com.example.marvellisimo.firebase.FirebaseFunctions
import com.example.marvellisimo.firebase.SharedMarvel
import com.example.marvellisimo.user.ListAllUserActivity
import com.google.firebase.auth.FirebaseAuth

class ComicActivity: AppCompatActivity() {
    private lateinit var shared: SharedMarvel
    lateinit var selectedComic : Comic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);
        val comic: Comic
        if(intent.hasExtra("SHARED_ITEM")){
            shared = intent.getParcelableExtra<SharedMarvel>("SHARED_ITEM")!!
            Picasso.get().load(Service.renamePathHttps(shared.thumbnail)).into(comicbook_cover_comic_page)
            comic_info_comic_page.text = if(shared.name != "") shared.name else shared.title
            comic_description_comic_page.text = if (shared.description != "") shared.description else "No description available"
            link_comic.movementMethod = LinkMovementMethod.getInstance()
            val text = "<a color:#e62429; href='${shared.url}'> Want to know more ? </a>"
            link_comic.text = Html.fromHtml(text)
        }
        if(intent.hasExtra(ComicViewHolder.SELECTED_COMIC)) {
            selectedComic = (intent.getParcelableExtra<Comic>(ComicViewHolder.SELECTED_COMIC) as? Comic)!!
            Picasso.get().load(Service.renamePathHttps(selectedComic.thumbnail?.path!!) + "." + selectedComic.thumbnail?.extension).into(comicbook_cover_comic_page)
            comic_info_comic_page.text = selectedComic.title
            comic_description_comic_page.text = if (!selectedComic.description.equals("null")) selectedComic.description else "No description available"
            link_comic.movementMethod = LinkMovementMethod.getInstance()
            val text = "<a color:#e62429; href='${selectedComic.urls?.url}'> Want to know more ? </a>"
            link_comic.text = Html.fromHtml(text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        Service._menu = menu!!
        Service.toggleNavbarItemsIfAuth(Service._menu)
        val favMenuItem: MenuItem? = menu?.findItem(R.id.Favorite)
        favMenuItem?.isVisible = false
        val share : MenuItem? = menu.findItem(R.id.share_icon)
        if(FirebaseAuth.getInstance().uid != null && !intent.hasExtra("SHARED_ITEM")) {
            share?.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.share_icon->{
                val intent = Intent(this, ListAllUserActivity::class.java)
                intent.putExtra("SHARED_COMIC", selectedComic)
                startActivity(intent)
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
                    "You clicked show all users",
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














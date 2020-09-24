package com.example.marvellisimo

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.data.Service
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comic.*
import com.example.marvellisimo.Model.Comic

class ComicActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val comic: Comic = intent.getParcelableExtra<Comic>(ComicViewHolder.SELECTED_COMIC) as Comic

        Picasso.get().load(Service.renamePathHttps(comic.thumbnail?.path!!) + "." + comic.thumbnail?.extension).into(comicbook_cover_comic_page)
        comic_info_comic_page.text = comic.title
        comic_description_comic_page.text = if (!comic.description.equals("null")) comic.description else "No description available"
        link_comic.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a color:#e62429; href='${comic.urls?.url}'> Want to know more ? </a>"
        link_comic.text = Html.fromHtml(text)

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
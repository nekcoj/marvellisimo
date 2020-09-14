package com.example.marvellisimo

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comic.*
import kotlinx.android.synthetic.main.single_character_view.*

class ComicActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val selectedComic = intent.getSerializableExtra(ComicViewHolder.SELECTED_COMIC) as? Comic

        Picasso.get().load(CharacterListAdapter.renamePathHttps(selectedComic?.thumbnail?.path.toString()) + "." + selectedComic?.thumbnail?.extension).into(comicbook_cover_comic_page)
        comic_info_comic_page.text = selectedComic?.title.toString()
        comic_description_comic_page.text = if (!selectedComic?.description.toString().equals("null")) selectedComic?.description.toString() else "No description available"
//        comic_description_comic_page.text = selectedComic?,
//        link.movementMethod = LinkMovementMethod.getInstance()
//        val text = "<a color:#e62429; href='${selectedCharacter?.urls?.get(0)?.url}'> Want to know more ? </a>"
//        link.text = Html.fromHtml(text)

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
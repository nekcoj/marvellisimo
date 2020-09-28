package com.example.marvellisimo.user

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.marvellisimo.R
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.firebase.SharedMarvel
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.model.Comic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_character_view.view.*
import kotlinx.android.synthetic.main.activity_character_view.view.characterName_adapter
import kotlinx.android.synthetic.main.activity_chat_view.*
import kotlinx.android.synthetic.main.activity_comic_list.view.*

class UserChat: AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)

        recyclerView_user_chat.adapter = adapter

        val user= intent.getParcelableExtra<User>("USER")
        supportActionBar?.title = user?.username
        listenForMessages()
    }

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val sharedObject = snapshot.getValue(SharedMarvel::class.java)
                if (sharedObject != null) {
                    if (sharedObject.fromId == FirebaseAuth.getInstance().uid) {
                        if (sharedObject.title == "") {
                            adapter.add(ShareCharacter(sharedObject))
                        } else {
                            adapter.add(ShareComic(sharedObject))
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }
}

class ShareCharacter(val character: SharedMarvel): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val imgComplete = Service.renamePathHttps(character.thumbnail)
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(viewHolder.itemView.character_adapter_img)
        viewHolder.itemView.characterName_adapter.text = character.name
        viewHolder.itemView.character_favIcon_adapter.isVisible = false
    }


    override fun getLayout(): Int {
        return R.layout.activity_character_view
    }
}

class ShareComic(val comic: SharedMarvel): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val imgComplete = Service.renamePathHttps(comic.thumbnail)
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(viewHolder.itemView.comic_list_cover_image)
        viewHolder.itemView.comic_list_item_title.text = comic.title
        viewHolder.itemView.comicFavIcon.isVisible = false
    }


    override fun getLayout(): Int {
        return R.layout.activity_comic_list
    }
}
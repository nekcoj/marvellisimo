package com.example.marvellisimo.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.activity.ComicActivity
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.firebase.SharedMarvel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_view.*
import kotlinx.android.synthetic.main.shared_from.view.*
import kotlinx.android.synthetic.main.shared_to.view.*

class UserChat: AppCompatActivity() {
    companion object {
        lateinit var user: User
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)

        recyclerView_user_chat.adapter = adapter

        user= intent.getParcelableExtra<User>("USER")!!
        supportActionBar?.title = user.username
        listenForMessages()
    }

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val sharedObject = snapshot.getValue(SharedMarvel::class.java)
                if (sharedObject != null) {
                    if (sharedObject.fromId == FirebaseAuth.getInstance().uid.toString() && sharedObject.toId == user.uId) {
                        adapter.add(ShareFrom(sharedObject))
                    } else if(sharedObject.fromId == user.uId && sharedObject.toId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ShareTo(sharedObject))
                    }
                    adapter.setOnItemClickListener { item, view ->
                        if(item.javaClass.simpleName == "ShareFrom"){
                            item as ShareFrom
                            val intent = Intent(view.context, ComicActivity::class.java)
                            intent.putExtra("SHARED_ITEM", item.shared)
                            startActivity(intent)
                        } else {
                            item as ShareTo
                            val intent = Intent(view.context, ComicActivity::class.java)
                            intent.putExtra("SHARED_ITEM", item.shared)
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }
}

class ShareFrom(val shared: SharedMarvel): Item<GroupieViewHolder>(){
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val nameOrTitle = if(shared.name != "") shared.name else shared.title
        val imgComplete = Service.renamePathHttps(shared.thumbnail)
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(viewHolder.itemView.shared_image)
        viewHolder.itemView.shared_title_name.text = nameOrTitle
        viewHolder.itemView.shared_sent_to.text = "Shared by me"
    }

    override fun getLayout(): Int {
        return R.layout.shared_to
    }
}

class ShareTo(val shared: SharedMarvel): Item<GroupieViewHolder>(){
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val nameOrTitle = if(shared.title != "") shared.title else shared.name
        val imgComplete = Service.renamePathHttps(shared.thumbnail)
        Picasso.get().load(imgComplete).placeholder(R.mipmap.marvel_logo_small).into(viewHolder.itemView.shared_image_from)
        viewHolder.itemView.shared_title_name_from.text = nameOrTitle
        viewHolder.itemView.shared_sent_from.text = "Shared by ${UserChat.user.username}"
    }

    override fun getLayout(): Int {
        return R.layout.shared_from
    }
}
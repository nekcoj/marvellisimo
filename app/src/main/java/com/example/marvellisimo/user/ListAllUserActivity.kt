package com.example.marvellisimo.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.marvellisimo.MainActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.SignInActivity
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.firebase.FirebaseFunctions
import com.example.marvellisimo.firebase.SharedMarvel
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.model.Comic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_list_all_user.*
import kotlinx.android.synthetic.main.activity_user_row.view.*

class ListAllUserActivity : AppCompatActivity() {
    companion object{
        var sharedObject : Any? = null

        fun shareMarvel( user : User, ){
            val fromId = FirebaseAuth.getInstance().uid
            val toId = user.uId
            if(fromId == null || toId == null) return
            val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
            var chatMessage: SharedMarvel = SharedMarvel()
            if(sharedObject?.javaClass?.simpleName.toString() == "Character") {
                val character = sharedObject as Character
                val thumbnail = character.thumbnail?.path + "." + character.thumbnail?.extension
                val url = character.urls?.url
                chatMessage = SharedMarvel(ref.key!!, fromId, toId, character.description!!, character.favorite!!, character.name!!, "", thumbnail, url!!, System.currentTimeMillis() / 1000, character.id!!)
            } else if (sharedObject?.javaClass?.simpleName.toString() == "Comic"){
                val comic = sharedObject as Comic
                val thumbnail = comic.thumbnail?.path + "." + comic.thumbnail?.extension
                val url = comic.urls?.url
                chatMessage = SharedMarvel(ref.key!!, fromId, toId, comic.description!!, comic.favorite!!, "", comic.title!!, thumbnail, url!!, System.currentTimeMillis() / 1000, comic.id!!)
            }

            ref.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("sharedMarvel", "Saved our chat message: ${ref.key}")
                }
                .addOnFailureListener(){
                    Log.d("sharedMarvell", "Fail to send !! ")
                }
        }
    }
    var user: FirebaseUser? = null
    var db: FirebaseDatabase? = null
    var usersListRef: DatabaseReference? = null
    var onlineStatus: DatabaseReference? = null
    var connectedRef: DatabaseReference? = null
    var userListValueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_user)

        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "All users"
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.mipmap.marvel_logo_small);
        supportActionBar?.setDisplayUseLogoEnabled(true);
        sharedObject = null

        if (intent.hasExtra("SHARED_CHARACTER")){
            sharedObject = intent.getParcelableExtra<Character>("SHARED_CHARACTER")!!
        } else if (intent.hasExtra("SHARED_COMIC")){
            sharedObject = intent.getParcelableExtra<Comic>("SHARED_COMIC")!!
        }

        db = FirebaseDatabase.getInstance()
        usersListRef = db!!.getReference("users")
        user = FirebaseAuth.getInstance().currentUser
        addToUserList(user)
        fetchUsers()
    }

    private fun addToUserList(user: FirebaseUser?) {
        usersListRef!!.child("${user!!.uid}/status").setValue("online")
        onlineStatus = db!!.getReference("users/" + user.uid + "/status")
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)!!
                if (connected) {
                    onlineStatus!!.onDisconnect().setValue("offline")
                    onlineStatus!!.setValue("online")
                } else {
                    onlineStatus!!.setValue("offline")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        userListValueEventListener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    Log.d("user", it.toString())
                    val user = it.getValue(User::class.java)
                    val uid = it.child("uid").getValue(String::class.java)
                    val username = it.child("username").getValue(String::class.java)
                    val status = it.child("status").getValue(String::class.java)
                    if (user != null && it.key != FirebaseAuth.getInstance().uid)
                        adapter.add(UserItem(User(uid!!, username!!, status!!)))
                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, UserChat::class.java)
                    intent.putExtra("USER", userItem.user)
                    startActivity(intent)
                }
                recycleview_listAllUser.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
        usersListRef!!.addValueEventListener(userListValueEventListener!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        Service.menu_ = menu!!
        Service.toggleNavbarItemsIfAuth(Service.menu_)
        val favMenuItem: MenuItem? = menu.findItem(R.id.Favorite)
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

class UserItem(val user: User): Item<GroupieViewHolder>(){
    @SuppressLint("ResourceAsColor")
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.itemView.username_textView.text = user.username
        if(user.status == "online")
            viewHolder.itemView.online_status.setBackgroundResource(R.drawable.status_indicator_online)
        if (ListAllUserActivity.sharedObject != null){
            viewHolder.itemView.share_button.isVisible = true
            viewHolder.itemView.share_button.setOnClickListener(){
                ListAllUserActivity.shareMarvel(user)
                viewHolder.itemView.share_button.isVisible = false
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_user_row
    }
}
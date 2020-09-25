package com.example.marvellisimo.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_list_all_user.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.activity_user_row.view.*

class ListAllUserActivity : AppCompatActivity() {

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

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("user", it.toString())
                    val user = it.getValue(User::class.java)
                    var uid = it.child("uid").getValue(String::class.java)
                    var username = it.child("username").getValue(String::class.java)
                    var status = it.child("status").getValue(String::class.java)
                    if (user != null && it.key != FirebaseAuth.getInstance().uid)
                        adapter.add(UserItem(User(uid!!, username!!, status!!)))
                }
                recycleview_listAllUser.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
class UserItem(val user: User): Item<ViewHolder>(){
    @SuppressLint("ResourceAsColor")
    override fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.username_textView.text = user.username
        if(user.status == "online")
            viewHolder.itemView.online_status.setBackgroundResource(R.drawable.status_indicator_online)
    }

    override fun getLayout(): Int {
        return R.layout.activity_user_row
    }
}
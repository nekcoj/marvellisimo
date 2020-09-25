package com.example.marvellisimo.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_view.*

class UserChat: AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)

        recyclerView_user_chat.adapter = adapter

        val user= intent.getParcelableExtra<User>("USER")
        supportActionBar?.title = user?.username

    }
}
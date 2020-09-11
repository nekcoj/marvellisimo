package com.example.marvellisimo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter: RecyclerView.Adapter<CustomViewHolder>()  {

    override fun getItemCount(): Int{
        return 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow =  layoutInflater.inflate(R.layout.character_view_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}

class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {
}
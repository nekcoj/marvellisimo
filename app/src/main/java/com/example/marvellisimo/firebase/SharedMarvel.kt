package com.example.marvellisimo.firebase

import android.os.Parcelable
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.model.Comic
import kotlinx.android.parcel.Parcelize

@Parcelize
class SharedMarvel(
    val id: String,
    val fromId: String,
    val toId: String,
    val description : String,
    val favorite : Boolean,
    val name: String,
    val title : String,
    val thumbnail : String,
    val url : String,
    val timeStamp : Long,
    val marvelId : Int
    ): Parcelable{
    constructor(): this("","","","",false,"","","","",-1,-1)
}


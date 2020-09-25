package com.example.marvellisimo.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uId: String, val username: String, val status: String): Parcelable {
    constructor() : this("","", "")
}


package com.example.marvellisimo.model

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Comic(
    @PrimaryKey
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var thumbnail: ThumbnailDTO? = null,
    var urls: UrlDTO? = null,
    var favorite : Boolean? = null
): RealmObject(), Parcelable, RealmModel

@Parcelize
open class UrlDTO(
    var type: String? = null,
    @PrimaryKey
    var url: String? = null,
): RealmObject(), Parcelable, RealmModel

@Parcelize
open class ThumbnailDTO(
    @PrimaryKey
    var path: String? = null,
    var extension: String? = null
) : RealmObject(), Parcelable, RealmModel

open class FavouriteList: RealmObject() {
    @PrimaryKey
    var id: Int? = null
}

@Parcelize
open class Character(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail: ThumbnailDTO? = null,
    var urls: UrlDTO? = null,
    var favorite : Boolean? = null
): RealmObject(), Parcelable, RealmModel
package com.example.marvellisimo.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Comic: RealmObject() {
    @PrimaryKey
    var id: Int? = null
    var title: String? = null
    var description: String? = null
    var thumbnail: ThumbnailDTO? = null
    var urls: UrlDTO? = null
    var favorite : Boolean? = null
}

open class UrlDTO(): RealmObject(){
    constructor(type: String?, url: String?): this(){
        this.type = type
        this.url = url
    }
    var type: String? = null
    @PrimaryKey
    var url: String? = null
}

open class ThumbnailDTO() : RealmObject(){
    constructor(path: String, extension: String) : this() {
        this.path = path
        this.extension = extension
}
    @PrimaryKey
    var path: String? = null
    var extension: String? = null
}

open class FavouriteList: RealmObject() {
    @PrimaryKey
    var id: Int? = null
}

open class Character: RealmObject(){
    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var description: String? = null
    var thumbnail: ThumbnailDTO? = null
    var urls: UrlDTO? = null
    var favorite : Boolean? = null
}
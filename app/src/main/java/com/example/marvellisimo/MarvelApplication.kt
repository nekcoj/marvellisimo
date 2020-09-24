package com.example.marvellisimo

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MarvelApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        var config = RealmConfiguration.Builder().name("MarvelData.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}
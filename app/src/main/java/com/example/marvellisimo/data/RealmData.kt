package com.example.marvellisimo.data

import com.example.marvellisimo.model.Comic
import com.example.marvellisimo.model.Character
import com.example.marvellisimo.model.FavouriteList
import com.example.marvellisimo.model.ThumbnailDTO
import com.example.marvellisimo.model.UrlDTO
import com.example.marvellisimo.realm
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmData {
    companion object{
        fun saveComic(comic: Comic) {
            val checkFav = getFavoriteIdList().contains(comic.id)
            val thumbnailNewComic = ThumbnailDTO(comic.thumbnail?.path, comic.thumbnail?.extension)
            val urlNewComic = UrlDTO(comic.urls?.type, comic.urls?.url)
            realm = Realm.getDefaultInstance()
            realm.use {r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(Comic().apply {
                        id = comic.id
                        title = comic.title
                        description = comic.description
                        thumbnail = thumbnailNewComic
                        urls = urlNewComic
                        favorite = checkFav
                    })
                }
            }
        }

        fun saveCharacter(character : Character){
            val checkFav = getFavoriteIdList().contains(character.id)
            val thumbnailNewCharacter = ThumbnailDTO(character.thumbnail?.path, character.thumbnail?.extension)
            val urlNewCharacter = UrlDTO(character.urls?.type, character.urls?.url)
            realm = Realm.getDefaultInstance()
            realm.use {r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(Character().apply {
                        id = character.id
                        name = character.name
                        description = character.description
                        thumbnail = thumbnailNewCharacter
                        urls = urlNewCharacter
                        favorite = checkFav
                    })
                }
            }
        }

        fun searchComic(searchString: String): RealmResults<Comic>? {
            realm = Realm.getDefaultInstance()
            val query = realm?.where(Comic::class.java)?.contains("title", searchString)?.findAllAsync()
            return query
        }

        fun searchCharacter(searchString: String): RealmResults<Character>? {
            realm = Realm.getDefaultInstance()
            val query = realm?.where(Character::class.java)?.contains("name", searchString)?.findAllAsync()
            return query
        }

        fun saveFavorite(marvelId: Int){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(FavouriteList().apply {
                        id = marvelId
                    })
                }
            }
        }

        fun removeFromFavorite(id: Int){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<FavouriteList>().equalTo("id", id)?.findAll()
                    query?.deleteAllFromRealm()
                }
            }
        }

        fun getFavoriteIdList():MutableList<Int>{
            val favIdList : MutableList<Int> = mutableListOf()
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<FavouriteList>().findAll()
                    for (q in query){
                        if (!favIdList.contains(q.id)){
                            favIdList.add(q.id!!)
                        }
                    }
                }
            }
            return favIdList
        }

        fun favoriteComics(): RealmResults<Comic>? {
            realm = Realm.getDefaultInstance()
            val query = realm?.where(Comic::class.java)?.equalTo("favorite", true)?.findAll()
            return query
        }

        fun favoriteCharacters(): RealmResults<Character>? {
            realm = Realm.getDefaultInstance()
            val query = realm?.where(Character::class.java)?.equalTo("favorite", true)?.findAll()
            return query
        }
    }
}
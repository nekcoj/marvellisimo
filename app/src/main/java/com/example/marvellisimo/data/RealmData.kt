package com.example.marvellisimo.data

import com.example.marvellisimo.ImageDTO
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.Model.Character
import com.example.marvellisimo.Model.FavouriteList
import com.example.marvellisimo.Model.ThumbnailDTO
import com.example.marvellisimo.Model.UrlDTO
import com.example.marvellisimo.Url
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

        fun saveCharacters(){
            Service.characterList.forEach { character ->
                val checkFav = getFavoriteIdList().contains(character.id)
                realm = Realm.getDefaultInstance()
                realm.use { r ->
                    r?.executeTransaction { realm ->
                        realm.insertOrUpdate(Character().apply {
                            id = character.id
                            name = character.name
                            description = character.description
                            thumbnail = ThumbnailDTO(character.thumbnail.path!!, character.thumbnail.extension!!)
                            urls = UrlDTO(character.urls?.get(0)?.type, character.urls?.get(0)?.url)
                            favorite = checkFav
                        })
                    }
                }
            }
        }
        fun readDataFromRealm(){
            readCharactersFromRealm()
        }

        private fun readCharactersFromRealm(){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<Character>().findAll()
                    for (character in query) {
                        val url: Url = Url(character?.urls?.type, character?.urls?.url)
                        val urls = arrayOf(url)
                        Service.characterList.add(com.example.marvellisimo.Character(
                            ImageDTO(character.thumbnail?.path, character.thumbnail?.extension),
                            character?.id,
                            character?.name,
                            character?.description,
                            urls,
                            character?.favorite
                        ))
                    }
                }
            }
        }

        fun searchComic(searchString: String): RealmResults<Comic>? {
            realm = Realm.getDefaultInstance()
            val query = realm?.where(Comic::class.java)?.contains("title", searchString)?.findAllAsync()
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
            var favIdList : MutableList<Int> = mutableListOf()
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
    }
}
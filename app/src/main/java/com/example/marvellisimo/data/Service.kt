package com.example.marvellisimo.data

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.*
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.Model.ThumbnailDTO
import com.example.marvellisimo.Model.UrlDTO
import io.realm.Realm
import io.realm.RealmResults

class Service {

    companion object{
        var limit: Int = 100
        var FavoriteModeOnComic = false
        var FavoriteModeOnCharacter = false
        var OffsetComics :Int = 0
        var OffsetCharacter :Int = 0
        var characterList: MutableList<Character> = mutableListOf()
        fun getAllFavoriteCharacters(characterList: MutableList<Character>):MutableList<Character>{
            val favoriteCharacters : MutableList<Character> = mutableListOf()
            if (characterList.size > 0){
                for (char in characterList){
                    if (char.favorite == true){
                        if (!favoriteCharacters.contains(char)){
                            favoriteCharacters.add(char)
                        }
                    }
                }
            }
            return favoriteCharacters
        }

        fun compareCharacterId(id:Int): Boolean{
            var isIdInList : Boolean = false
            characterList.forEach{ char ->
                if (char.id == id){
                    isIdInList = true
                }
            }
            return isIdInList
        }

        fun checkIfFavoriteToggled(menu: MenuItem?, comicOrCharacter: String?) {
            if (comicOrCharacter == "comic"){
                if (FavoriteModeOnComic) {
                    menu?.setIcon(android.R.drawable.btn_star_big_on)
                } else {
                    menu?.setIcon(android.R.drawable.btn_star_big_off)
                }
            } else if(comicOrCharacter == "character") {
                if (FavoriteModeOnCharacter) {
                    menu?.setIcon(android.R.drawable.btn_star_big_on)
                } else {
                    menu?.setIcon(android.R.drawable.btn_star_big_off)
                }
            }
        }

        fun convertFromMarvelDataToRealmData(comic: com.example.marvellisimo.Comic): Comic {
            val thumbnail = ThumbnailDTO(comic.thumbnail.path.toString(), comic.thumbnail.extension.toString())
            val urls = UrlDTO(comic.urls?.get(0)?.type.toString(), comic.urls?.get(0)?.url.toString())
            val newComic: Comic = Comic(comic.id, comic.title.toString(), comic.description.toString(), thumbnail, urls, comic.favorite)
            return newComic
        }

        fun renamePathHttps(path: String): String {
            return path.replace("http", "https")
        }

        fun changeFavoriteStatus(id: Int?) {
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransactionAsync { realm ->
                    realm.where(Comic::class.java).equalTo("id", id).findFirst()?.apply {
                        favorite = !favorite!!
                    }
                }
            }
        }

        @SuppressLint("CheckResult")
        fun subscribeToRealm(realmResults: RealmResults<Comic>?, rv: RecyclerView){
            realmResults?.asFlowable()?.subscribe {
                rv.adapter = ComicListAdapter(it)
            }
        }
    }
}



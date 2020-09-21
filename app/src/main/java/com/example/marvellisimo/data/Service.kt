package com.example.marvellisimo.data
import android.util.Log
import android.view.MenuItem
import com.example.marvellisimo.Character
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.Model.ThumbnailDTO
import com.example.marvellisimo.Model.UrlDTO

class Service {

    companion object{
        var limit: Int = 20
        var FavoriteModeOnComic = false
        var FavoriteModeOnCharacter = false
        var OffsetComics :Int = 0
        var OffsetCharacter :Int = 0
        var comicList: MutableList<com.example.marvellisimo.Model.Comic> = mutableListOf()
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
        //check to see if object should be added to comic/character list. TRUE = Comic, FALSE = Character
        fun addToList(id: Int, isComic_Character: Boolean): Boolean{
            var checked: Boolean
            if(isComic_Character) {
                comicList.forEach { comic -> if(comic.id == id)
                    return true
                }
            } else {
                characterList.forEach { character -> if(character.id == id)
                    return true
                }
            }
            return false
        }

        fun getAllFavoriteComics(comicList: MutableList<Comic>): MutableList<Comic>{
            val favoriteComics: MutableList<Comic> = mutableListOf()
            if (comicList.size > 0){
                for (comic in comicList){
                    if (comic.favorite == true){
                        if (!favoriteComics.contains(comic)){
                            favoriteComics.add(comic)
                        }
                    }
                }
            }
            return favoriteComics
        }

        fun compareComicId(id :Int) : Boolean{
            var isId :Boolean = false
            comicList.forEach{ comic ->
                if (comic.id == id){
                    isId = true
                }
            }
            return isId
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
            var comicId = comic.id
            var comicTitle = comic.title
            Log.d("__convertFromMtoRData", "$comicId : $comicTitle")
            val thumbnail = ThumbnailDTO(comic.thumbnail.path.toString(), comic.thumbnail.extension.toString())
            val urls = UrlDTO(comic.urls?.get(0)?.type.toString(), comic.urls?.get(0)?.url.toString())
            Log.d("__convertFromMtoRData", "${comic.id}")
            val newComic: Comic = Comic(comic.id, comic.title.toString(), comic.description.toString(), thumbnail, urls, comic.favorite)
            Log.d("__newComic", "${newComic.toString()}")
            return newComic
        }
    }
}



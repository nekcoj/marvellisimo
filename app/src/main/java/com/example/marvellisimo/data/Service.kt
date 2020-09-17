package com.example.marvellisimo.data
import com.example.marvellisimo.Character
import com.example.marvellisimo.Comic

class Service {

    companion object{
        var limit: Int = 20
        var FavoriteModeOnComic = false
        var FavoriteModeOnCharacter = false
        var OffsetComics :Int = 0
        var OffsetCharacter :Int = 0
        var comicList: MutableList<Comic> = mutableListOf()
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

        fun getAllFavoriteComics(comicList: MutableList<Comic>): MutableList<Comic>{
            var favoriteComics: MutableList<Comic> = mutableListOf()
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

    }
}



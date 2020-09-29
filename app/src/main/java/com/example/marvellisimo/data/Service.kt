package com.example.marvellisimo.data

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.*
import com.example.marvellisimo.adapter.CharacterListAdapter
import com.example.marvellisimo.model.Comic
import com.example.marvellisimo.model.ThumbnailDTO
import com.example.marvellisimo.model.UrlDTO
import com.example.marvellisimo.adapter.ComicListAdapter
import com.example.marvellisimo.model.Character
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmResults

class Service {

    companion object{
        var limit: Int = 100
        var FavoriteModeOnComic = false
        var FavoriteModeOnCharacter = false
        var OffsetComics :Int = 0
        var OffsetCharacter :Int = 0
        lateinit var _menu: Menu
        fun checkIfFavoriteToggled(menu: MenuItem?, comicOrCharacter: String?) {
            if (comicOrCharacter == "comic"){
                if (FavoriteModeOnComic) {
                    menu?.setIcon(R.mipmap.arc_icon_on2)
                } else {
                    menu?.setIcon(R.mipmap.arc_icon_off)
                }
            } else if(comicOrCharacter == "character") {
                if (FavoriteModeOnCharacter) {
                    menu?.setIcon(R.mipmap.arc_icon_on2)
                } else {
                    menu?.setIcon(R.mipmap.arc_icon_off)
                }
            }
        }

        fun convertComicFromMarvelDataToRealmData(comic: com.example.marvellisimo.marvel.Comic): Comic {
            val thumbnail = ThumbnailDTO(comic.thumbnail.path.toString(), comic.thumbnail.extension.toString())
            val urls = UrlDTO(comic.urls?.get(0)?.type.toString(), comic.urls?.get(0)?.url.toString())
            val newComic: Comic = Comic(comic.id, comic.title.toString(), comic.description.toString(), thumbnail, urls, comic.favorite)
            return newComic
        }

        fun convertCharacterFromMarvelDataToRealmData(character: com.example.marvellisimo.marvel.Character): Character {
            val thumbnail = ThumbnailDTO(character.thumbnail.path.toString(), character.thumbnail.extension.toString())
            val urls = UrlDTO(character.urls?.get(0)?.type.toString(), character.urls?.get(0)?.url.toString())
            val newCharacter: Character = Character(character.id, character.name.toString(), character.description.toString(), thumbnail, urls, character.favorite)
            return newCharacter
        }

        fun renamePathHttps(path: String): String {
            return path.replace("http", "https")
        }

        fun changeComicFavoriteStatus(id: Int?) {
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransactionAsync { realm ->
                    realm.where(Comic::class.java).equalTo("id", id).findFirst()?.apply {
                        favorite = !favorite!!
                    }
                }
            }
        }

        fun changeCharacterFavoriteStatus(id: Int?) {
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransactionAsync { realm ->
                    realm.where(Character::class.java).equalTo("id", id).findFirst()?.apply {
                        favorite = !favorite!!
                    }
                }
            }
        }

        @SuppressLint("CheckResult")
        fun subscribeToComicRealm(realmResults: RealmResults<Comic>?, rv: RecyclerView){
            realmResults?.asFlowable()?.subscribe {
                rv.adapter = ComicListAdapter(it)
            }
        }

        @SuppressLint("CheckResult")
        fun subscribeToCharacterRealm(realmResults: RealmResults<Character>?, rv: RecyclerView){
            realmResults?.asFlowable()?.subscribe {
                rv.adapter = CharacterListAdapter(it)
            }
        }

        fun toggleNavbarItemsIfAuth(menu: Menu?) {
            val signedIn: MenuItem? = menu?.findItem(R.id.Sign_in)
            val signedInMenu: MenuItem? = menu?.findItem(R.id.Sign_in_text)
            val showAllUsers: MenuItem? = menu?.findItem(R.id.Show_all_users)
            val logout: MenuItem? = menu?.findItem(R.id.Log_Out)
            val share : MenuItem? = menu?.findItem(R.id.share_icon)
            if(FirebaseAuth.getInstance().uid != null) {
                signedIn?.isVisible = false
                signedInMenu?.isVisible = false
                showAllUsers?.isVisible = true
                logout?.isVisible = true
                share?.isVisible = false
            } else {
                signedIn?.isVisible = true
                signedInMenu?.isVisible = true
                showAllUsers?.isVisible = false
                logout?.isVisible = false
                share?.isVisible = false
            }
        }
    }
}



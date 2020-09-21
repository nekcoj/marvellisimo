
import android.util.Log
import com.example.marvellisimo.ImageDTO
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.Model.Character
import com.example.marvellisimo.Model.FavouriteList
import com.example.marvellisimo.Model.ThumbnailDTO
import com.example.marvellisimo.Model.UrlDTO
import com.example.marvellisimo.Url
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.realm
import io.realm.Realm
import io.realm.kotlin.where

class RealmData {
    companion object{
        fun saveComics() {
            Service.comicList.forEach { comic ->
                var checkFav = getFavoriteIdList().contains(comic.id)
                realm = Realm.getDefaultInstance()
                realm.use { r ->
                    r?.executeTransaction { realm ->
                        realm.insertOrUpdate(Comic().apply {
                            id = comic.id
                            title = comic.title
                            description = comic.description
                            thumbnail = ThumbnailDTO(comic.thumbnail.path!!, comic.thumbnail.extension!!)
                            urls = UrlDTO(comic.urls?.get(0)?.type, comic.urls?.get(0)?.url)
                            favorite = checkFav
                        })
                    }
                }
            }
        }

        fun saveCharacters(){
            Service.characterList.forEach { character ->
                var checkFav = getFavoriteIdList().contains(character.id)
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
            readComicsFromRealm()
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

        private fun readComicsFromRealm(){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    val query = realm.where<Comic>().findAll()
                    for (comic in query) {
                        val url: Url = Url(comic?.urls?.type, comic?.urls?.url)
                        val urls = arrayOf(url)
                        Service.comicList.add(com.example.marvellisimo.Comic(
                            comic?.favorite,
                            ImageDTO(comic.thumbnail?.path, comic.thumbnail?.extension),
                            comic?.title,
                            comic?.description,
                            comic?.id,
                            urls,
                        ))
                    }
                }
            }
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
    }
}
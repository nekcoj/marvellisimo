
import com.example.marvellisimo.Model.Comic
import com.example.marvellisimo.Model.FavouriteList
import com.example.marvellisimo.Model.ThumbnailDTO
import com.example.marvellisimo.Model.UrlDTO
import com.example.marvellisimo.data.Service
import com.example.marvellisimo.realm
import io.realm.Realm
import io.realm.kotlin.where

class RealmData {
    companion object{
        fun saveComics() {
            Service.comicList.forEach { comic ->
                realm = Realm.getDefaultInstance()
                realm.use { r ->
                    r?.executeTransaction { realm ->
                        realm.insertOrUpdate(Comic().apply {
                            id = comic.id
                            title = comic.title
                            description = comic.description
                            thumbnail = ThumbnailDTO(comic.thumbnail.path, comic.thumbnail.extension)
                            urls = UrlDTO(comic.urls[0].type, comic.urls[0].url)
                        })
                    }
                }
            }
        }

        fun saveCharacters(){
            Service.characterList.forEach { character ->
                realm = Realm.getDefaultInstance()
                realm.use { r ->
                    r?.executeTransaction { realm ->
                        realm.insertOrUpdate(com.example.marvellisimo.Model.Character().apply {
                            id = character.id
                            name = character.name
                            description = character.description
                            thumbnail = ThumbnailDTO(character.thumbnail.path, character.thumbnail.extension)
                            urls = UrlDTO(character.urls[0].type, character.urls[0].url)
                        })
                    }
                }
            }
        }
        fun readDataFromRealm(){
//            readCharactersFromRealm()
//            readComicsFromRealm()
        }
//        private fun readCharactersFromRealm(){
//            realm = Realm.getDefaultInstance()
//            realm.use { r ->
//                r?.executeTransaction { realm ->
//                    val query = realm.where<Character>().findAll()
//                    for (q in query) {
//                        Service.characterList.add(q )
//                    }
//                }
//            }
//        }

//        private fun readComicsFromRealm() {
//            realm = Realm.getDefaultInstance()
//            realm.use { r ->
//                r?.executeTransaction { realm ->
//                    val query = realm.where<Comic>().findAll()
//                    for (q in query) {
//
//                    }
//                }
//            }
//        }

        fun saveFavorite(comicId: Int){
            realm = Realm.getDefaultInstance()
            realm.use { r ->
                r?.executeTransaction { realm ->
                    realm.insertOrUpdate(FavouriteList().apply {
                        id = comicId
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
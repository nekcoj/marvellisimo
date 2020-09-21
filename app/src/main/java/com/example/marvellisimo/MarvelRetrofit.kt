package com.example.marvellisimo

import android.annotation.SuppressLint
import android.util.Log
import com.example.marvellisimo.data.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MarvelRetrofit {
    private const val LOG = false
    private const val PUBLIC_KEY = "c270132add73d86a82e678586b4010b6"
    private const val PRIVATE_KEY = "8b2030d913a6f8f2148b5525c5c644db797b4551"
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    val marvelService: MarvelService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(MarvelService::class.java)

    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = if (LOG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val builder = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                val original = chain.request()
                val originalHttpUrl = original.url
                val timestamp = System.currentTimeMillis().toString()
                val hash = (timestamp + PRIVATE_KEY + PUBLIC_KEY).md5()

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", PUBLIC_KEY)
                    .addQueryParameter("ts",timestamp)
                    .addQueryParameter("hash", hash)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
        return builder.build()
    }

    private fun String.md5(): String {
        val md5 = "MD5"
        try { // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(md5)
            digest.update(this.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("CheckResult")
    fun getAllComics(search: String? = null) {
        marvelService.getAllComics(limit = Service.limit, offset = Service.OffsetComics, titleStartsWith = search?.toLowerCase())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("__", "Error getAllComics " + err.message)
                else {
                    result.data.results.forEach { comic ->
                        if(!Service.compareComicId(comic.id!!)){
                            comic.favorite = RealmData.getFavoriteIdList().contains(comic.id)
                            Service.comicList.add(comic)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getAllCharacters(search : String? = null){
        marvelService.getAllCharacters(limit = Service.limit, offset = Service.OffsetCharacter, nameStartsWith = search?.toLowerCase())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    result.data.results.forEach { character ->
                        if(! Service.compareCharacterId(character.id!!)) {
                            character.favorite = RealmData.getFavoriteIdList().contains(character.id)
                            Service.characterList.add(character)
                        }

                    }
                }
            }
    }

    fun getAllFavorite(){
        for(id in RealmData.getFavoriteIdList()){
            Log.d("_", "Fav id : ${id} will fetch")
            if (!getAllFavComics(id)){
                getAllFavCharacter(id)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getAllFavCharacter(id : Int) : Boolean {
        var fetch: Boolean = false
        marvelService.getCharacter(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) {
                    Log.d("_", "Error getAllFavCharacters " + err.message)
                }
                else {
                    result.data.results.forEach { character ->
                        if(!Service.compareCharacterId(character.id!!)) {
                            Service.characterList.add(character)
                        }
                    }
                    fetch = true
                }
            }
        return fetch
    }

    @SuppressLint("CheckResult")
    fun getAllFavComics(id : Int): Boolean {
    var fetch: Boolean = false
    marvelService.getComics(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) {
                    Log.d("__", "Error getAllFavComics " + err.message)
                }
                else {
                    result.data.results.forEach { comic ->
                        if(!Service.compareComicId(comic.id!!)){
                            Service.comicList.add(comic)
                        }
                    }
                   fetch = true
                }
            }
        return fetch
    }




}

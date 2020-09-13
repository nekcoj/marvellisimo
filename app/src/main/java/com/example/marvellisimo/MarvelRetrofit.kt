package com.example.marvellisimo

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MarvelRetrofit {
    private const val LOG = false
    private const val PUBLIC_KEY = "9e31cec40957c0bc8eaa3c9a3256645c"
    private const val PRIVATE_KEY = "8b4f888188613f05582276cf4e2bd1a30bca16e0"
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
    fun getAllComics() {
        marvelService.getAllComics(limit = Limit.comics, offset = Offset.comics)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    Log.d("___", "I got a CharacterDataWrapper $result")
                    result.data.results.forEach { comic ->
                        if(!ComicList.comics.contains(comic)){
                            ComicList.comics.add(comic)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getAllCharacters(){
        marvelService.getAllCharacters(limit = 30, offset = 0)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null) Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    result.data.results.forEach { character ->
                        if(!charList.characters.contains(character)) {
                            charList.characters.add(character)
                        }
                        Log.d("__", "characters list size :" + charList.characters.size.toString())
                        Log.d("__", character.name.toString())
                    }
                }
            }
    }
}

object ComicList {
    var comics: MutableList<Comic> = mutableListOf()
}

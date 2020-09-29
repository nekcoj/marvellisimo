package com.example.marvellisimo.marvel

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelService {
    @GET("characters")
    fun getAllCharacters(
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("name") byExactName: String? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Single<CharacterDataWrapper>

    @GET("comics")
    fun getAllComics(
        @Query("titleStartsWith") titleStartsWith: String?  =null,
        @Query("title") byExactTitle: String? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Single<ComicDataWrapper>

    @GET("characters/{characterId}")
    fun getCharacter(
        @Path("characterId") characterId: Int? = null
    ): Single<CharacterDataWrapper>

    @GET("comics/{comicId}")
    fun getComics(
        @Path("comicId") comicId: Int? = null
    ): Single<ComicDataWrapper>
}
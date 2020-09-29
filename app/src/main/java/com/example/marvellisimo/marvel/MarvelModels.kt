package com.example.marvellisimo.marvel


import java.io.Serializable

class MarvelModels {
}
data class CharacterDataWrapper(
    val code: Int, // optional): The HTTP status code of the returned result.,
    val status: String, // optional): A string description of the call status.,
    val data: CharacterDataContainer // optional): The results returned by the call.,
)

data class CharacterDataContainer(
    val total: Int, //, optional): The total number of resources available given the current filter set.,
    val count: Int, //, optional): The total number of results returned by this call.,
    val results: Array<Character> //, optional): The list of characters returned by the call.
)

data class Character  (
    val thumbnail: ImageDTO, //, optional): The representative image for this character.,
    var id: Int?, //, optional): The unique ID of the character resource.,
    var name: String?, //, optional): The name of the character.,
    val description: String?, //, optional): A short bio or description of the character.,
    val urls: Array<Url>? = null,
    var favorite : Boolean? = true
) : Serializable

data class ComicDataWrapper (
    val data: ComicDataContainer //(ComicDataContainer, optional): The results returned by the call.,
)

data class ComicDataContainer (
    val results: Array<Comic> //(Array[Comic], optional): The list of comics returned by the call
)

data class Comic (
    var favorite : Boolean? = false,
    val thumbnail: ImageDTO, //(Image, optional): The representative image for this comic.,
    val title: String?, //(string, optional): The canonical title of the comic.,
    val description: String?, //(string, optional): The preferred description of the comic.,
    val id: Int?, //(int, optional): The unique ID of the comic resource.,
    val urls: Array<Url>? = null//(Array[Url], optional): A set of public web site URLs for the resource.,
) : Serializable

data class ImageDTO (
    val path: String?,
    val extension: String?
) : Serializable

data class Url (
    val type: String?,
    val url: String?
) : Serializable


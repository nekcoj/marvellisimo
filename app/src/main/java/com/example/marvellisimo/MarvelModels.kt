package com.example.marvellisimo

import android.media.Image
import java.io.Serializable

class MarvelModels {
}
data class CharacterDataWrapper(
    // val copyright: String, // optional): The copyright notice for the returned result.,
    // val attributionText: String, // optional): The attribution notice for this result. Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.,
    // val attributionHTML: String, // optional): An HTML representation of the attribution notice for this result. Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.,
    // val etag: String // optional): A digest value of the content returned by the call.
    val code: Int, // optional): The HTTP status code of the returned result.,
    val status: String, // optional): A string description of the call status.,
    val data: CharacterDataContainer // optional): The results returned by the call.,
)

data class CharacterDataContainer(
    // val offset: Int, //, optional): The requested offset (number of skipped results) of the call.,
    // val limit: Int, //, optional): The requested result limit.,
    val total: Int, //, optional): The total number of resources available given the current filter set.,
    val count: Int, //, optional): The total number of results returned by this call.,
    val results: Array<Character> //, optional): The list of characters returned by the call.
)

data class Character  (
    /*val modified: Date, //, optional): The date the resource was most recently modified.,
    val resourceURI: string, //, optional): The canonical URL identifier for this resource.,
    val urls: Array, //[Url], optional): A set of public web site URLs for the resource.,
    val comics: ComicList, //, optional): A resource list containing comics which feature this character.,
    val stories: StoryList, //, optional): A resource list of stories in which this character appears.,
    val events: EventList, //, optional): A resource list of events in which this character appears.,
    val series: SeriesList, //, optional): A resource list of series in which this character appears.*/
    val thumbnail: ImageDTO, //, optional): The representative image for this character.,
    val id: Int, //, optional): The unique ID of the character resource.,
    val name: String, //, optional): The name of the character.,
    val description: String, //, optional): A short bio or description of the character.,
    val urls: Array<Url>,
    var favorite : Boolean? = true
) : Serializable



data class ComicDataWrapper (
    val data: ComicDataContainer //(ComicDataContainer, optional): The results returned by the call.,
    //code (int, optional): The HTTP status code of the returned result.,
    //status (string, optional): A string description of the call status.,
    //copyright (string, optional): The copyright notice for the returned result.,
    //attributionText (string, optional): The attribution notice for this result. Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.,
    //attributionHTML (string, optional): An HTML representation of the attribution notice for this result. Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.,
    //etag (string, optional): A digest value of the content returned by the call.
)

data class ComicDataContainer (
    //val limit: Int //(int, optional): The requested result limit.,
    val results: Array<Comic> //(Array[Comic], optional): The list of comics returned by the call
    //offset (int, optional): The requested offset (number of skipped results) of the call.,
    //total (int, optional): The total number of resources available given the current filter set.,
    //count (int, optional): The total number of results returned by this call.,
)

data class Comic (
    var favorite : Boolean? = false,
    val thumbnail: ImageDTO, //(Image, optional): The representative image for this comic.,
    val title: String, //(string, optional): The canonical title of the comic.,
    val description: String, //(string, optional): The preferred description of the comic.,
    val id: Int, //(int, optional): The unique ID of the comic resource.,
    val urls: Array<Url>//(Array[Url], optional): A set of public web site URLs for the resource.,
    /*
    digitalId (int, optional): The ID of the digital comic representation of this comic. Will be 0 if the comic is not available digitally.,
    issueNumber (double, optional): The number of the issue in the series (will generally be 0 for collection formats).,
    variantDescription (string, optional): If the issue is a variant (e.g. an alternate cover, second printing, or director’s cut), a text description of the variant.,
    modified (Date, optional): The date the resource was most recently modified.,
    isbn (string, optional): The ISBN for the comic (generally only populated for collection formats).,
    upc (string, optional): The UPC barcode number for the comic (generally only populated for periodical formats).,
    diamondCode (string, optional): The Diamond code for the comic.,
    ean (string, optional): The EAN barcode for the comic.,
    issn (string, optional): The ISSN barcode for the comic.,
    format (string, optional): The publication format of the comic e.g. comic, hardcover, trade paperback.,
    pageCount (int, optional): The number of story pages in the comic.,
    textObjects (Array[TextObject], optional): A set of descriptive text blurbs for the comic.,
    resourceURI (string, optional): The canonical URL identifier for this resource.,
    series (SeriesSummary, optional): A summary representation of the series to which this comic belongs.,
    variants (Array[ComicSummary], optional): A list of variant issues for this comic (includes the "original" issue if the current issue is a variant).,
    collections (Array[ComicSummary], optional): A list of collections which include this comic (will generally be empty if the comic's format is a collection).,
    collectedIssues (Array[ComicSummary], optional): A list of issues collected in this comic (will generally be empty for periodical formats such as "comic" or "magazine").,
    dates (Array[ComicDate], optional): A list of key dates for this comic.,
    prices (Array[ComicPrice], optional): A list of prices for this comic.,
    images (Array[Image], optional): A list of promotional images associated with this comic.,
    creators (CreatorList, optional): A resource list containing the creators associated with this comic.,
    characters (CharacterList, optional): A resource list containing the characters which appear in this comic.,
    stories (StoryList, optional): A resource list containing the stories which appear in this comic.,
    events (EventList, optional): A resource list containing the events in which this comic appears.*/
) : Serializable

data class ImageDTO (
    val path: String,
    val extension: String
) : Serializable

data class Url (
    val type: String,
    val url: String
) : Serializable


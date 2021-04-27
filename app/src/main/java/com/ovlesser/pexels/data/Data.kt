package com.ovlesser.pexels.data

import com.ovlesser.pexels.database.DatabasePexelsPhoto
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class Data(
    val page: Int,
    @Json(name = "per_page") val perPage: Int,
    val photos: List<Photo>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "next_page") val nextPage: String
) {
    data class Photo(
        val id: Int,
        val width: Int,
        val height: Int,
        val url: String,
        val photographer: String,
        @Json(name = "photographer_url") val photographerUrl: String,
        @Json(name = "photographer_id") val photographerId: Int,
        @Json(name = "avg_color") val avgColor: String,
        val src: Src?,
        val liked: Boolean,
    ) {
        data class Src(
            val original: String,
            val large2x: String,
            val large: String,
            val medium: String,
            val small: String,
            val portrait: String,
            val landscape: String,
            val tiny: String
        )
    }
}

fun List<Data.Photo>.asDatabaseModel(): List<DatabasePexelsPhoto> {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(Data.Photo.Src::class.java)

    return map {
        DatabasePexelsPhoto(
            id = it.id.toString(),
            width = it.width,
            height = it.height,
            url = it.url,
            photographer = it.photographer,
            photographerUrl = it.photographerUrl,
            photographerId = it.photographerId,
            avgColor = it.avgColor,
            src =jsonAdapter.toJson(it.src),
            liked = it.liked
        )
    }
}
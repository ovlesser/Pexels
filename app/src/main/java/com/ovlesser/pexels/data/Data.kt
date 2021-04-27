package com.ovlesser.pexels.data

import android.os.Parcelable
import com.ovlesser.pexels.database.DatabasePexelsPhoto
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val page: Int = 0,
    @Json(name = "per_page") val perPage: Int = 0,
    val photos: List<Photo>,
    @Json(name = "total_results") val totalResults: Int = 0,
    @Json(name = "next_page") val nextPage: String = ""
): Parcelable {
    @Parcelize
    data class Photo(
        val id: Int,
        val width: Int = 0,
        val height: Int = 0,
        val url: String = "",
        val photographer: String = "",
        @Json(name = "photographer_url") val photographerUrl: String = "",
        @Json(name = "photographer_id") val photographerId: Int = 0,
        @Json(name = "avg_color") val avgColor: String = "",
        val src: Src? = null,
        val liked: Boolean = false,
    ): Parcelable {
        @Parcelize
        data class Src(
            val original: String = "",
            val large2x: String = "",
            val large: String = "",
            val medium: String = "",
            val small: String = "",
            val portrait: String = "",
            val landscape: String = "",
            val tiny: String = ""
        ): Parcelable
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
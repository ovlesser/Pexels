package com.ovlesser.pexels.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ovlesser.pexels.data.Data
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity
class DatabasePexelsPhoto constructor(
    @PrimaryKey
    val id: String,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val photographer: String = "",
    val photographerUrl: String = "",
    val photographerId: Int = 0,
    val avgColor: String = "",
    val src: String = "",
    val liked: Boolean = false)

fun List<DatabasePexelsPhoto>.asDomainModel(): List<Data.Photo> {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(Data.Photo.Src::class.java)

    return map {
        Data.Photo(
            id = it.id.toInt(),
            width = it.width,
            height = it.height,
            url = it.url,
            photographer = it.photographer,
            photographerUrl = it.photographerUrl,
            photographerId = it.photographerId,
            avgColor = it.avgColor,
            src = jsonAdapter.fromJson(it.src),
            liked = it.liked)
    }
}
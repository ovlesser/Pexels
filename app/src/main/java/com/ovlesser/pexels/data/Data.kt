package com.ovlesser.pexels.data

import com.squareup.moshi.Json

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
        val src: Src,
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
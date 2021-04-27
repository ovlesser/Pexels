package com.ovlesser.pexels.network

import com.ovlesser.pexels.data.Data
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private val BASE_URL = "https://api.pexels.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PexelsApiService {
    @GET("search")
    @Headers("Authorization:563492ad6f917000010000011a5094093e6e4ee3978287979ad139ac")
    fun getData(@Query("query") keyword: String,
                @Query("page") pageIndex: Int = 0,
                @Query("per_page") perPage: Int = 20): Call<Data>

    @GET("search")
    @Headers("Authorization:563492ad6f917000010000011a5094093e6e4ee3978287979ad139ac")
    suspend fun getDataCoroutine(@Query("query") keyword: String,
                                 @Query("page") pageIndex: Int = 0,
                                 @Query("per_page") perPage: Int = 20): Data
}

object PexelApi {
    val retrofitService: PexelsApiService by lazy {
        retrofit.create(PexelsApiService::class.java)
    }
}

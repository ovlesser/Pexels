package com.ovlesser.pexels.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.data.asDatabaseModel
import com.ovlesser.pexels.database.PexelsPhotoDatabase
import com.ovlesser.pexels.database.asDomainModel
import com.ovlesser.pexels.network.PexelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PexelsPhotoRepository(private val database: PexelsPhotoDatabase) {

    val data: LiveData<Data>
        get() {
            val photos = database.pexelsPhotoDao.getPhotos()
            return Transformations.map(photos) {
                Data(
                    page = 0,
                    perPage = 0,
                    photos = it.asDomainModel(),
                    totalResults = it.size,
                    nextPage = ""
                )
            }
        }

    suspend fun refreshPexelsPhoto(keyword: String) {
        withContext(Dispatchers.IO) {
            val data = PexelApi.retrofitService.getDataCoroutine(keyword)
            database.pexelsPhotoDao.insertAll(data.photos.asDatabaseModel())
        }
    }
}
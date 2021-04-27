package com.ovlesser.pexels.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.data.asDatabaseModel
import com.ovlesser.pexels.database.PexelsPhotoDatabase
import com.ovlesser.pexels.database.asDomainModel
import com.ovlesser.pexels.network.PexelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PexelsPhotoRepository(private val database: PexelsPhotoDatabase) {
    val _data = MutableLiveData<Data>()
    val data: LiveData<Data>
        get() {
            val photos = database.pexelsPhotoDao.getPhotos()
            return Transformations.map(photos) {
                Data(
                    page = _data.value?.page ?: 0,
                    perPage = _data.value?.perPage ?: 0,
                    photos = it.asDomainModel(),
                    totalResults = it.size,
                    nextPage = _data.value?.nextPage ?: ""
                )
            }
        }

    suspend fun refreshPexelsPhoto(keyword: String, pageIndex: Int) {
        lateinit var data: Data
        withContext(Dispatchers.IO) {
            data = PexelApi.retrofitService.getDataCoroutine(keyword, pageIndex = pageIndex)
            database.pexelsPhotoDao.insertAll(data.photos.asDatabaseModel())
        }
        _data.value = data
    }
}
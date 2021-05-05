package com.ovlesser.pexels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.data.asDatabaseModel
import com.ovlesser.pexels.database.DatabasePexelsPhoto
import com.ovlesser.pexels.database.PexelsPhotoDao
import com.ovlesser.pexels.network.PexelsApiService
import kotlinx.coroutines.channels.Channel
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MockCall<T>(result: Data): Call<T> {
    override fun clone(): Call<T> {
        TODO("Not yet implemented")
    }

    override fun execute(): Response<T> {
        TODO("Not yet implemented")
    }

    override fun enqueue(callback: Callback<T>) {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}

class MockApiService(var result: Data): PexelsApiService {
    override fun getData(keyword: String, pageIndex: Int, perPage: Int) = MockCall<Data>(result)

    override suspend fun getDataCoroutine(keyword: String, pageIndex: Int, perPage: Int) = result
}

class MockDao( val data: Data): PexelsPhotoDao {
    private val _photos = MutableLiveData<List<DatabasePexelsPhoto>>()

    override fun getPhotos(): LiveData<List<DatabasePexelsPhoto>> {
        _photos.value = data.photos.asDatabaseModel()
        return _photos
    }

    override fun insertAll(photos: List<DatabasePexelsPhoto>) {
        _photos.value = photos
    }

    override fun clearAll() {
        _photos.value = emptyList()
    }
}
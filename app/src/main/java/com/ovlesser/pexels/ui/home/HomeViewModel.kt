package com.ovlesser.pexels.ui.home

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.data.sampleData
import com.ovlesser.pexels.database.getDatabase
import com.ovlesser.pexels.network.PexelApi
import com.ovlesser.pexels.repository.PexelsPhotoRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class PexelsApiStatus { LOADING, ERROR, DONE}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val pexelsPhotoRepository = PexelsPhotoRepository(getDatabase(application))

    // The internal MutableLiveData Data that stores the most recent data
    private val _data = MutableLiveData<Data>()
    private val _response = MutableLiveData<String>()
    private val _status = MutableLiveData<PexelsApiStatus>()

    // The external immutable LiveData for the response Data
    val data = pexelsPhotoRepository.data

    val response: LiveData<String>
        get() = _response

    val status: LiveData<PexelsApiStatus>
        get() = _status

    init {
//        getDataFromSample()
//        getDataFromNetwork()
//        getDataFromNetworkCoroutine()
        refreshRepository()
    }

    private fun getDataFromSample() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(Data::class.java)
        val sampleData = jsonAdapter.fromJson(sampleData)
        _data.value = sampleData ?: Data(0, 0, emptyList(), 0, "")
        _response.value = "Success: init with sample data"
        _status.value = PexelsApiStatus.DONE
    }

    private fun getDataFromNetwork(keyword: String = "") {
        if (keyword.isEmpty()) {
            _status.value = PexelsApiStatus.DONE
        } else {
            _status.value = PexelsApiStatus.LOADING
            PexelApi.retrofitService.getData(keyword = keyword).enqueue(
                object : Callback<Data> {
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        _data.value = response.body()
                        _response.value = "Success: Pexel data about ${keyword} is fetched"
                        _status.value = PexelsApiStatus.DONE
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        _data.value = Data(0, 0, emptyList(), 0, "")
                        _response.value = "Failure: ${t.message}"
                        _status.value = PexelsApiStatus.ERROR
                    }
                }
            )
        }
    }

    private fun getDataFromNetworkCoroutine(keyword: String = "") {
        if (keyword.isEmpty()) {
            _status.value = PexelsApiStatus.DONE
        } else {
            _status.value = PexelsApiStatus.LOADING
            viewModelScope.launch {
                try {
                    _data.value = PexelApi.retrofitService.getDataCoroutine(keyword = keyword)
                    _response.value = "Success: Pexel data about ${keyword} is fetched"
                    _status.value = PexelsApiStatus.DONE
                } catch (e: Exception) {
                    _data.value = Data(0, 0, emptyList(), 0, "")
                    _response.value = "Failure: ${e.message}"
                    _status.value = PexelsApiStatus.ERROR
                }
            }
        }
    }

    fun refreshRepository(keyword: String = "") {
        if (keyword.isEmpty()) {
            _status.value = PexelsApiStatus.DONE
        } else {
            val nextPageUri = data.value?.nextPage?.toUri()
            val pageIndex = nextPageUri?.getQueryParameter("page")?.toInt() ?: 0
            _status.value = PexelsApiStatus.LOADING
            viewModelScope.launch {
                try {
                    pexelsPhotoRepository.refreshPexelsPhoto(keyword, pageIndex = pageIndex)
                    _response.value = "Success: Pexel data about ${keyword} is fetched"
                    _status.value = PexelsApiStatus.DONE
                } catch (e: Exception) {
                    _data.value = Data(0, 0, emptyList(), 0, "")
                    _response.value = "Failure: ${e.message}"
                    _status.value = PexelsApiStatus.ERROR
                }
            }
        }
    }

    private fun clearDataFromDatabase() {
        viewModelScope.launch {
            try {
                pexelsPhotoRepository.clearDatabase()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
                _status.value = PexelsApiStatus.DONE
            }
        }
    }

    fun updateKeyword( keyword: String) {
        clearDataFromDatabase()
        refreshRepository(keyword)
    }

    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
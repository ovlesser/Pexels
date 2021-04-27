package com.ovlesser.pexels.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.data.sampleData
import com.ovlesser.pexels.network.PexelApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    // The internal MutableLiveData Data that stores the most recent data
    private val _data = MutableLiveData<Data>()

    // The external immutable LiveData for the response Data
    val data: LiveData<Data>
        get() = _data

    init {
//        getDataFromSample()
        getDataFromNetwork()
    }

    private fun getDataFromSample() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(Data::class.java)
        val sampleData = jsonAdapter.fromJson(sampleData)
        _data.value = sampleData ?: Data(0, 0, emptyList(), 0, "")
    }

    private fun getDataFromNetwork() {
        PexelApi.retrofitService.getData(keyword = "panda").enqueue(
            object: Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    _data.value = response.body()
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    _data.value = Data(0, 0, emptyList(), 0, "")
                }
            }
        )
    }
}
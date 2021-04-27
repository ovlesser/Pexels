package com.ovlesser.pexels.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.ovlesser.pexels.data.Data

class DetailViewModel(photo: Data.Photo, app: Application): AndroidViewModel(app) {
    private val _photo = MutableLiveData<Data.Photo>()
    val photo: LiveData<Data.Photo>
        get() = _photo

    val size = Transformations.map(this.photo) {
        "${it.width} * ${it.height}"
    }

    init {
        _photo.value = photo
    }

    class Factory( private val photo: Data.Photo,
                   private val app: Application): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel( photo, app) as T
            }
            throw IllegalArgumentException("Unknown View class")
        }

    }
}
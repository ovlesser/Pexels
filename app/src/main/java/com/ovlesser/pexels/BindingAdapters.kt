package com.ovlesser.pexels

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.ui.home.PexelsApiStatus
import com.ovlesser.pexels.ui.home.PhotoGridAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     photos: List<Data.Photo>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(photos)
}

@BindingAdapter("imageUrl")
fun bindImageView(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imageView)
    }
}

@BindingAdapter("pexelsApiStatus")
fun bindStatus(statusImageView: ImageView,
               status: PexelsApiStatus) {
    when (status) {
        PexelsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        PexelsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        PexelsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
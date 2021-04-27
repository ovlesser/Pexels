package com.ovlesser.pexels.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ovlesser.pexels.data.Data
import com.ovlesser.pexels.databinding.GridViewItemBinding

class PhotoGridAdapter(private val onClickListener: OnClickListener,
                       private val onScrollToBottom: () -> Unit): ListAdapter<Data.Photo, PhotoGridAdapter.PhotoGridViewHolder>(DiffCallback) {

    class PhotoGridViewHolder(private var binding: GridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Data.Photo) {
            binding.photo = photo
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (photo: Data.Photo) -> Unit) {
        fun onClick( photo: Data.Photo) = clickListener( photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridViewHolder {
        return PhotoGridViewHolder( GridViewItemBinding.inflate( LayoutInflater.from( parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) {
        val photo = getItem(position)
        if (position == itemCount - 1) {
            onScrollToBottom()
        }
        holder.itemView.setOnClickListener {
            onClickListener.onClick(photo)
        }
        holder.bind(photo)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Data.Photo>() {
        override fun areItemsTheSame(oldItem: Data.Photo, newItem: Data.Photo): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Data.Photo, newItem: Data.Photo): Boolean {
            return oldItem == newItem
        }

    }
}

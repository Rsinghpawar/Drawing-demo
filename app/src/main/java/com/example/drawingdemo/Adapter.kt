package com.codinginflow.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.ItemRecyclerViewBinding
import com.google.android.material.snackbar.Snackbar

class UnsplashPhotoAdapter : PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(
    PHOTO_COMPARATOR
) {
    private lateinit var onItemClickListener: OnItemClickListener
    fun setListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class PhotoViewHolder( val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: UnsplashPhoto?) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo!!.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)

                tvUserName.text = photo?.user?.username
                btnFav.setOnClickListener {
                    onItemClickListener.favButtonClick(photo!! , btnFav)
                }

            }
        }

    }
    interface OnItemClickListener {
        fun favButtonClick(photo: UnsplashPhoto,btnFav: ImageView)
        fun isImageSaved(photo: UnsplashPhoto , btnFav: ImageView)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
              false

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean = false

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
        //return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        onItemClickListener.isImageSaved(currentItem!! , holder.binding.btnFav)
        if (currentItem != null)
            holder.bind(currentItem)
    }


}

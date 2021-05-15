package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.malec.cheesetime.databinding.ItemPhotoBinding
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.BindingListAdapter

class PhotoAdapter(private val vm: PhotoAction) :
    BindingListAdapter<Photo, ItemPhotoBinding>(diffUtilCallback) {
    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.content.hashCode() == newItem.content.hashCode() &&
                        oldItem.ref == newItem.ref
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemPhotoBinding.inflate(inflater, parent, false)

    override fun onBindViewHolder(
        holder: BindingListAdapter<Photo, ItemPhotoBinding>.ItemViewHolder,
        position: Int
    ) {
        val photo = getItem(position)

        holder.binding.photo = photo
        photo.content?.let {
            holder.binding.photoImage.setImageBitmap(it)
        }
    }

    override fun onCreateViewHolder(binding: ItemPhotoBinding) {
        binding.root.setOnLongClickListener {
            binding.photo?.let {
                vm.onPhotoLongClick(it)
            }
            true
        }
        binding.root.setOnClickListener {
            binding.photo?.let {
                vm.onPhotoClick(it)
            }
        }
    }

    interface PhotoAction {
        fun onPhotoClick(photo: Photo)

        fun onPhotoLongClick(photo: Photo)
    }
}
package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.databinding.ItemPhotoBinding
import com.malec.cheesetime.model.Photo

class PhotoAdapter(private val vm: PhotoAction) :
    ListAdapter<Photo, PhotoAdapter.PhotoItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Photo>() {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPhotoBinding = ItemPhotoBinding.inflate(inflater, parent, false)
        return PhotoItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        val photo = getItem(position)

        holder.binding?.photo = photo
        photo.content?.let {
            holder.binding?.photoImage?.setImageBitmap(it)
        }
    }

    inner class PhotoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemPhotoBinding? = DataBindingUtil.bind(view)

        init {
            binding?.root?.setOnLongClickListener {
                binding.photo?.let {
                    vm.onPhotoLongClick(it)
                }
                true
            }
        }
    }

    interface PhotoAction {
        fun onPhotoLongClick(photo: Photo)
    }
}
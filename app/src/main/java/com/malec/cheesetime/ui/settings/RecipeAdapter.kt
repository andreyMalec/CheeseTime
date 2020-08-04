package com.malec.cheesetime.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.databinding.ItemRecipeBinding
import com.malec.cheesetime.model.Recipe

class RecipeAdapter(private val vm: RecipeAction) :
    ListAdapter<Recipe, RecipeAdapter.RecipeItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.stages == newItem.stages
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRecipeBinding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {
        val recipe = getItem(position)

        holder.binding?.recipe = recipe
    }

    inner class RecipeItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemRecipeBinding? = DataBindingUtil.bind(view)

        init {
            binding?.root?.setOnClickListener {
                binding.recipe?.let {
                    vm.onClick(it)
                }
            }

            binding?.removeButton?.setOnClickListener {
                binding.recipe?.let {
                    vm.onRemove(it)
                }
            }
        }
    }

    interface RecipeAction {
        fun onClick(recipe: Recipe)

        fun onRemove(recipe: Recipe)
    }
}
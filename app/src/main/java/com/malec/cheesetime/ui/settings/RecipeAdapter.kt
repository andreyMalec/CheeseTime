package com.malec.cheesetime.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.malec.cheesetime.databinding.ItemRecipeBinding
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.ui.BindingListAdapter

class RecipeAdapter(private val vm: RecipeAction) :
    BindingListAdapter<Recipe, ItemRecipeBinding>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemRecipeBinding.inflate(inflater, parent, false)

    override fun onBindViewHolder(
        holder: BindingListAdapter<Recipe, ItemRecipeBinding>.ItemViewHolder,
        position: Int
    ) {
        val recipe = getItem(position)

        holder.binding.recipe = recipe
    }

    override fun onCreateViewHolder(binding: ItemRecipeBinding) {
        binding.root.setOnClickListener {
            binding.recipe?.let {
                vm.onClick(it)
            }
        }

        binding.removeButton.setOnClickListener {
            binding.recipe?.let {
                vm.onRemove(it)
            }
        }
    }

    interface RecipeAction {
        fun onClick(recipe: Recipe)

        fun onRemove(recipe: Recipe)
    }
}
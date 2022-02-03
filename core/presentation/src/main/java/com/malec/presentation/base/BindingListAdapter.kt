package com.malec.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener

abstract class BindingListAdapter<Model, ModelDataBinding : ViewDataBinding> :
    RecyclerView.Adapter<BindingListAdapter<Model, ModelDataBinding>.ItemViewHolder> {

    private val mDiffer: AsyncListDiffer<Model>
    private val mListener = ListListener<Model> { previousList, currentList ->
        onCurrentListChanged(previousList, currentList)
    }

    val currentList: List<Model>
        get() = mDiffer.currentList

    protected constructor(diffCallback: DiffUtil.ItemCallback<Model>) {
        mDiffer = AsyncListDiffer(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )
        mDiffer.addListListener(mListener)
    }

    protected constructor(config: AsyncDifferConfig<Model>) {
        mDiffer = AsyncListDiffer(AdapterListUpdateCallback(this), config)
        mDiffer.addListListener(mListener)
    }

    protected abstract fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ModelDataBinding

    protected abstract fun onBindViewHolder(binding: ModelDataBinding, position: Int)

    protected open fun onCurrentListChanged(previousList: List<Model>, currentList: List<Model>) {}

    protected open fun onCreateViewHolder(binding: ModelDataBinding) {}

    open fun submitList(list: List<Model?>?) {
        mDiffer.submitList(list)
    }

    fun submitList(list: List<Model>?, commitCallback: Runnable?) {
        mDiffer.submitList(list, commitCallback)
    }

    protected fun getItem(position: Int): Model {
        return mDiffer.currentList[position]
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        onBindViewHolder(holder.binding, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflateBinding(inflater, parent))
    }

    inner class ItemViewHolder(val binding: ModelDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            onCreateViewHolder(binding)
        }
    }
}
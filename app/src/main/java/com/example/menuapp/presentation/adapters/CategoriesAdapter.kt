package com.example.menuapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.databinding.ItemCategoriesBinding
import com.example.menuapp.domain.entities.CategoryEntity

class CategoriesAdapter(
    private val colorTextSelected: Int,
    private val colorBackgroundSelected: Int,
    private val colorTextUnselected: Int,
    private val colorBackgroundUnselected: Int,
    private val onItemSelectedCallback: (
        itemName: String
    ) -> Unit
) : ListAdapter<CategoryEntity, CategoriesAdapter.CategoriesViewHolder>(CategoriesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = ItemCategoriesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.category.text = item.name
        setCategorySelected(item.selected, holder)
    }

    inner class CategoriesViewHolder(val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemSelectedCallback(getItem(adapterPosition).name)
            }
        }
    }

    private class CategoriesDiffUtil : DiffUtil.ItemCallback<CategoryEntity>() {
        override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem == newItem
        }
    }

    private fun setCategorySelected(selected: Boolean, holder: CategoriesViewHolder) {
        val textColor = if (selected) colorTextSelected else colorTextUnselected
        val backGroundColor = if (selected) colorBackgroundSelected else colorBackgroundUnselected
        val elevation = if (selected) 0.0f else 16.0f
        holder.binding.category.setTextColor(textColor)
        with(holder.binding.categoryContainer) {
            setCardBackgroundColor(backGroundColor)
            this.elevation = elevation
        }
    }
}
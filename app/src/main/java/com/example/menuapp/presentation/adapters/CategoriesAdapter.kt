package com.example.menuapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.R
import com.example.menuapp.databinding.ItemCategoriesBinding
import com.example.menuapp.domain.entities.CategoryEntity

class CategoriesAdapter(
    private val context: Context?,
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
        if (item.selected) setCategorySelected(holder) else setCategoryUnselected(holder)
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
            return oldItem.selected == newItem.selected
        }
    }

    private fun setCategorySelected(holder: CategoriesViewHolder) {
        context ?: throw RuntimeException("Provided context is null")
        holder.binding.category
            .setTextColor(ContextCompat.getColor(context, R.color.pink))
        with(holder.binding.categoryContainer) {
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_pink))
            elevation = 0.0f
        }
    }

    private fun setCategoryUnselected(holder: CategoriesViewHolder) {
        context ?: throw RuntimeException("Provided context is null")
        holder.binding.category
            .setTextColor(ContextCompat.getColor(context, R.color.light_gray))
        with(holder.binding.categoryContainer) {
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            elevation = 16.0f
        }
    }
}
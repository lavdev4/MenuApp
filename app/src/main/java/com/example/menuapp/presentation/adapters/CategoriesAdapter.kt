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
        context ?: throw RuntimeException("Provided context is null")
        val item = getItem(position)
        holder.binding.category.text = item.name
        if (item.selected) {
            holder.binding.category
                .setTextColor(ContextCompat.getColor(context, R.color.pink))
            with(holder.binding.categoryContainer) {
                setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_pink))
                elevation = 0.0f
            }
        } else {
            holder.binding.category
                .setTextColor(ContextCompat.getColor(context, R.color.light_gray))
            with(holder.binding.categoryContainer) {
                setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                elevation = 16.0f
            }
        }
    }

//    override fun onBindViewHolder(
//        holder: ExampleViewHolder,
//        position: Int,
//        payloads: MutableList<Any>,
//    ) {
//        if (payloads.isNotEmpty()) {
//            val bundle = (payloads[0] as Bundle)
//            with(holder.binding) {
//                textView.text = bundle.getBoolean(PAYLOAD_PARAMETER_1)
//                switch.isChecked = bundle.getBoolean(PAYLOAD_PARAMETER_2)
//                button.isEnabled = bundle.getBoolean(PAYLOAD_PARAMETER_3)
//            }
//        } else {
//            super.onBindViewHolder(holder, position, payloads)
//        }
//    }

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
}
package com.example.menuapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.databinding.ItemMealsListBinding
import com.example.menuapp.domain.entities.MealEntity
import com.squareup.picasso.Picasso

class MealsAdapter : ListAdapter<MealEntity, MealsAdapter.MealsViewHolder>(MealsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val binding = ItemMealsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            title.text = item.name
            info.text = item.instruction
            Picasso.get().load(item.img).into(image)
        }
    }

    inner class MealsViewHolder(val binding: ItemMealsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class MealsDiffUtil : DiffUtil.ItemCallback<MealEntity>() {
        override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return oldItem == newItem
        }
    }
}
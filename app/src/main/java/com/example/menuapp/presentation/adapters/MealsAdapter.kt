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

    inner class MealsViewHolder(val binding: ItemMealsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class MealsDiffUtil : DiffUtil.ItemCallback<MealEntity>() {
        override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return (oldItem.name == newItem.name) &&
                    (oldItem.instruction == newItem.instruction) &&
                    (oldItem.category == newItem.category) &&
                    (oldItem.img == newItem.img)
        }

//        override fun getChangePayload(oldItem: Data, newItem: Data): Any {
//            return Bundle().apply {
//                putString(PAYLOAD_PARAMETER_1, newItem.parameter1)
//                putBoolean(PAYLOAD_PARAMETER_2, newItem.parameter2)
//                putBoolean(PAYLOAD_PARAMETER_3, newItem.parameter3)
//            }
//        }
    }

//    companion object {
//        const val PAYLOAD_PARAMETER_1 = "param1"
//        const val PAYLOAD_PARAMETER_2 = "param2"
//        const val PAYLOAD_PARAMETER_3 = "param3"
//    }
}
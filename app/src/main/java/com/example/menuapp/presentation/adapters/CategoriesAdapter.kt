package com.example.menuapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.databinding.ItemCategoriesBinding

class CategoriesAdapter(
    private val onItemSelectedCallback: (
        itemName: String
    ) -> Unit
) : ListAdapter<String, CategoriesAdapter.CategoriesViewHolder>(CategoriesDiffUtil()) {

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
        holder.binding.category.text = item
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
            binding.category.setOnCheckedChangeListener { button, checked ->
                if (checked) {
                    onItemSelectedCallback(getItem(adapterPosition))
                    button.elevation = 0f
                } else button.elevation = 15f
            }
        }
    }

    private class CategoriesDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
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
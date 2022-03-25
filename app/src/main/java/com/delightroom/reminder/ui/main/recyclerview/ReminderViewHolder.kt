package com.delightroom.reminder.ui.main.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.delightroom.reminder.databinding.RecyclerviewItemBinding
import com.delightroom.reminder.repository.ReminderData

class ReminderViewHolder(private val binding: RecyclerviewItemBinding, onItemClick: (ReminderData) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: ReminderData

    init {
        itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    fun bind(item: ReminderData) {
        binding.alarm = item
        binding.executePendingBindings()
    }
}

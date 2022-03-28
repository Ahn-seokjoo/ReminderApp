package com.delightroom.reminder.ui.main.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.delightroom.reminder.databinding.RecyclerviewItemBinding
import com.delightroom.reminder.repository.ReminderData

class ReminderViewHolder(
    private val binding: RecyclerviewItemBinding, onItemClick: (ReminderData) -> Unit, onItemLongClick: (ReminderData) -> Unit,
    private val onEnableButtonClick: (ReminderData) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: ReminderData

    init {
        itemView.setOnClickListener {
            onItemClick(item)
        }
        itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    fun bind(item: ReminderData) {
        this.item = item
        binding.apply {
            alarm = item
            executePendingBindings()
        }
        binding.alarmEnabled.setOnClickListener {
            onEnableButtonClick(item)
        }
    }
}

package com.delightroom.reminder.ui.main.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.delightroom.reminder.databinding.RecyclerviewItemBinding
import com.delightroom.reminder.repository.ReminderData

class ReminderRecyclerviewAdapter(
    private val onItemClick: (ReminderData) -> Unit, private val onItemLongClick: (ReminderData) -> Unit,
    private val onEnableButtonClick: (ReminderData) -> Unit
) : ListAdapter<ReminderData, ReminderViewHolder>(ReminderDiffUtil) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(binding, onItemClick, onItemLongClick, onEnableButtonClick)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ReminderDiffUtil : DiffUtil.ItemCallback<ReminderData>() {
        override fun areItemsTheSame(oldItem: ReminderData, newItem: ReminderData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReminderData, newItem: ReminderData): Boolean {
            return oldItem == newItem
        }

    }
}

package com.delightroom.reminder.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delightroom.reminder.R
import com.delightroom.reminder.repository.ReminderData

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("bindTime")
    fun TextView.bindTime(time: String) {
        val times: List<Int> = time.split(":").map { it.toInt() }
        text = if (times[0] > 12) {
            String.format("%02d:%02d PM", times[0] - 12, times[1])
        } else {
            String.format("%02d:%02d AM", times[0], times[1])
        }
    }

    @JvmStatic
    @BindingAdapter("bindImage")
    fun ImageView.bindImage(activate: Boolean) {
        if (activate) {
            setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.remind_check_true, context.theme))
        } else {
            setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.remind_check_false, context.theme))
        }
    }

    @JvmStatic
    @BindingAdapter("bindData")
    fun RecyclerView.bindData(data: NonNullLiveData<List<ReminderData>>) {
        (adapter as ListAdapter<Any, RecyclerView.ViewHolder>).submitList(data.value.toList())
    }
}

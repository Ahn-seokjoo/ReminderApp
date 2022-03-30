package com.delightroom.reminder.util

import android.widget.TimePicker

object StringUtils {
    fun getTimeResult(time: String, hour: Boolean) = if (hour) time.split(":")[0].toInt() else time.split(":")[1].toInt()
    fun convertTimeFormat(timePicker: TimePicker) = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

    const val REMIND = "remind"
    const val ALARM_REMIND = "alarmRemind"
}

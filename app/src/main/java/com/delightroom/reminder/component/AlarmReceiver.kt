package com.delightroom.reminder.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.util.StringUtils

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmIntent = Intent(context, AlarmService::class.java)
        alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        alarmIntent.putExtras(bundleOf(StringUtils.REMIND to intent.extras?.getParcelable<ReminderData>(StringUtils.ALARM_REMIND)))
        context.startService(alarmIntent)
    }
}

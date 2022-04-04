package com.delightroom.reminder.component

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.delightroom.reminder.repository.local.LocalRemindDataSource
import com.delightroom.reminder.util.StringUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var local: LocalRemindDataSource
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val job = Job()
            CoroutineScope(job).launch {
                local.getAll()
                    .filter {
                        it.activate
                    }.forEach { reminder ->
                        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                            putExtra(StringUtils.ALARM_REMIND, reminder)
                        }
                        val pendingIntent =
                            PendingIntent.getBroadcast(
                                context,
                                reminder.id,
                                alarmIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )

                        val calendar = Calendar.getInstance().apply {
                            timeZone = TimeZone.getTimeZone("Asia/Seoul")
                            this[Calendar.HOUR_OF_DAY] = StringUtils.getTimeResult(reminder.time, hour = true)
                            this[Calendar.MINUTE] = StringUtils.getTimeResult(reminder.time, hour = false)
                            this[Calendar.SECOND] = 0
                            this[Calendar.MILLISECOND] = 0
                        }

                        val nowCalendar = Calendar.getInstance()
                        if (calendar.before(nowCalendar) || nowCalendar.time == calendar.time) {
                            calendar.add(Calendar.DATE, 1)
                        }

                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                job.cancel()
            }
        }
    }
}

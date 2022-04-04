package com.delightroom.reminder.component

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.delightroom.reminder.R
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.ui.AlarmActivity
import com.delightroom.reminder.util.StringUtils

class AlarmService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val data = intent.extras?.getParcelable<ReminderData>(StringUtils.REMIND) ?: throw NullPointerException("service에서 NPE!")
        val alarmIntent = Intent(this, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtras(bundleOf(StringUtils.ALARM_REMIND to data))
        }

        val pendingIntent = PendingIntent.getActivity(this, data.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(data.id.toString(), "알림", NotificationManager.IMPORTANCE_HIGH)

            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(applicationContext, data.id.toString())
                .setContentTitle("알림")
                .setSmallIcon(R.drawable.alarm_image)
                .setContentText(data.remind)
                .setContentIntent(pendingIntent)

            notificationManager.notify(data.id, notification.build())
            startForeground(data.id, notification.build())
        }
        return START_REDELIVER_INTENT
    }

}

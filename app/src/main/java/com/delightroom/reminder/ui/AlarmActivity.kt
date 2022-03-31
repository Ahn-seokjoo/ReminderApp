package com.delightroom.reminder.ui

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.delightroom.reminder.R
import com.delightroom.reminder.component.AlarmReceiver
import com.delightroom.reminder.databinding.ActivityAlarmBinding
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.util.StringUtils
import com.delightroom.reminder.viewmodel.ReminderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private val reminderViewModel: ReminderViewModel by viewModels()
    private var _binding: ActivityAlarmBinding? = null
    private val binding: ActivityAlarmBinding get() = _binding!!
    private lateinit var ringtonePlayer: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        val data = intent.getParcelableExtra<ReminderData>(StringUtils.ALARM_REMIND) ?: throw NullPointerException("intent가 비어있어요..!!")

        binding.apply {
            lifecycleOwner = this@AlarmActivity
            remind = data
        }
        ringtonePlay(data.ringtone)
        initDismissButtonClick(data)
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun initDismissButtonClick(data: ReminderData) {
        binding.dismissButton.setOnClickListener {
            reminderViewModel.updateRemind(data.copy(activate = !data.activate))
            cancelAlarm(data.id)
            finish()
        }
    }

    private fun cancelAlarm(id: Int) {
        val alarmManager = this.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun ringtonePlay(ringtone: String) {
        ringtonePlayer = RingtoneManager.getRingtone(applicationContext, ringtone.toUri())
        ringtonePlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtonePlayer.stop()
        binding.unbind()
        _binding = null
    }
}

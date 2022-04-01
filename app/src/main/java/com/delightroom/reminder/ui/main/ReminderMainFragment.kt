package com.delightroom.reminder.ui.main

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.delightroom.reminder.R
import com.delightroom.reminder.base.BaseFragment
import com.delightroom.reminder.component.AlarmReceiver
import com.delightroom.reminder.databinding.FragmentMainReminderBinding
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.ui.main.recyclerview.ReminderRecyclerviewAdapter
import com.delightroom.reminder.util.StringUtils
import com.delightroom.reminder.viewmodel.ReminderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReminderMainFragment : BaseFragment<FragmentMainReminderBinding>(R.layout.fragment_main_reminder) {
    private val reminderViewModel: ReminderViewModel by activityViewModels()
    private val reminderAdapter by lazy { ReminderRecyclerviewAdapter(::startEditReminder, ::deleteReminder, ::enableClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recyclerview.adapter = reminderAdapter
            vm = reminderViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        startNewReminder()
    }

    /** 추가 버튼 클릭시 새 추가 페이지로 이동합니다. */
    private fun startNewReminder() {
        binding.addReminder.setOnClickListener {
            findNavController().navigate(R.id.action_reminderMainFragment_to_reminderEditingFragment)
        }
    }

    /** 아이템을 클릭 시 ringtone data는 viewmodel에 잠시 저장하고, 수정할 수 있게 클릭한 reminder 정보와 함께 수정 페이지로 이동합니다. */
    private fun startEditReminder(reminder: ReminderData) {
        reminderViewModel.setRingtoneData(reminder.ringtone.toUri())
        findNavController().navigate(R.id.action_reminderMainFragment_to_reminderEditingFragment, bundleOf("reminder" to reminder))
    }

    /** 클릭된 아이템의 정보는 모두 동일하고, activate만 바꿔줍니다. */
    private fun enableClickListener(reminder: ReminderData) {
        reminderViewModel.updateRemind(
            reminder.copy(activate = !reminder.activate)
        )
        if (reminder.activate) {
            cancelAlarm(reminder.id)
        } else {
            registerAlarm(reminder)
        }
    }

    private fun registerAlarm(reminder: ReminderData) {
        val alarmManager = activity?.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("remindData", reminder)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, reminder.id, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
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

    private fun cancelAlarm(id: Int) {
        val alarmManager = activity?.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    /** 아이템 롱클릭시에 삭제 dialog를 생성합니다. */
    private fun deleteReminder(reminder: ReminderData) {
        AlertDialog.Builder(context)
            .setMessage("삭제 하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                reminderViewModel.deleteRemind(reminder.id)
                cancelAlarm(reminder.id)
            }
            .setNegativeButton("아니오") { _, _ ->
            }
            .create()
            .show()
    }
}

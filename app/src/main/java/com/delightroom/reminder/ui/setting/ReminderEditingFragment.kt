package com.delightroom.reminder.ui.setting

import android.app.Activity.ALARM_SERVICE
import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.delightroom.reminder.R
import com.delightroom.reminder.base.BaseFragment
import com.delightroom.reminder.component.AlarmReceiver
import com.delightroom.reminder.databinding.FragmentEditingReminderBinding
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.util.StringUtils
import com.delightroom.reminder.viewmodel.ReminderViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ReminderEditingFragment : BaseFragment<FragmentEditingReminderBinding>(R.layout.fragment_editing_reminder) {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val reminderViewModel: ReminderViewModel by activityViewModels()
    private val args by lazy { arguments?.getParcelable<ReminderData>("reminder") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditTextCount()
        setEditData()
        selectRingtone()
        setSelectedRingtoneData()
        clickSaveButton()
    }

    private fun setEditTextCount() {
        binding.editNameRemind.textChanges()
            .subscribe {
                binding.editRemindTextCount.text = "${binding.editNameRemind.length()}/28"
            }.addTo(compositeDisposable)
    }

    private fun setEditData() {
        args?.let { remindData ->
            binding.editNameRemind.setText(remindData.remind)
            binding.selectedRingtone.text = getRingtoneTitle(remindData.ringtone.toUri())
            binding.timePicker.hour = StringUtils.getTimeResult(remindData.time, true)
            binding.timePicker.minute = StringUtils.getTimeResult(remindData.time, false)
        }
    }

    private fun selectRingtone() {
        binding.ringtoneList.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            getResult.launch(intent)
        }
    }

    /** 현재 페이지에서 선택한 ringtone의 정보를 viewmodel에 저장합니다. */
    private fun setSelectedRingtoneData() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                reminderViewModel.setRingtoneData(uri)
                binding.selectedRingtone.text = getRingtoneTitle(uri)
            }
        }
    }

    private fun getRingtoneTitle(ringtone: Uri?): String {
        return RingtoneManager.getRingtone(context, ringtone).getTitle(context)
    }

    private fun clickSaveButton() {
        with(binding) {
            saveButton.setOnClickListener {
                if (editNameRemind.text.isNullOrEmpty()) {
                    showAlertMessage()
                } else {
                    /** 이전 fragment에서 정보를 보내서 여기로 왔다 = 수정 페이지, 아니다 = 새로 추가 페이지 구분 합니다.
                     *  특히 remind의 경우 null일 수가 없기 때문에 args의 remind의 null 여부로 판단합니다. */
                    if (args?.remind.isNullOrEmpty()) {
                        addRemind()
                    } else {
                        updateRemind()
                    }
                    reminderViewModel.setRingtoneData(ReminderViewModel.DEFAULT_RINGTONE.toUri())
                    findNavController().navigate(R.id.action_reminderEditingFragment_to_reminderMainFragment)
                }
            }
        }
    }

    private fun setAlarm(remind: ReminderData) {
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(StringUtils.ALARM_REMIND, remind)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, remind.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
            this[Calendar.HOUR_OF_DAY] = StringUtils.getTimeResult(remind.time, hour = true)
            this[Calendar.MINUTE] = StringUtils.getTimeResult(remind.time, hour = false)
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

    private fun showAlertMessage() {
        AlertDialog.Builder(context)
            .setMessage("리마인더 이름은 반드시 포함되어야 합니다")
            .setPositiveButton("예") { _, _ ->
            }
            .create()
            .show()
    }

    private fun FragmentEditingReminderBinding.addRemind() {
        lifecycleScope.launch {
            val add = ReminderData(
                time = StringUtils.convertTimeFormat(timePicker),
                remind = editNameRemind.text.toString(),
                activate = ACTIVATE,
                ringtone = reminderViewModel.ringtone.value
            )
            val id = reminderViewModel.addRemind(add).toInt()
            setAlarm(add.copy(id = id))
        }
    }

    private fun FragmentEditingReminderBinding.updateRemind() {
        val update = ReminderData(
            time = StringUtils.convertTimeFormat(timePicker),
            remind = editNameRemind.text.toString(),
            activate = ACTIVATE,
            ringtone = reminderViewModel.ringtone.value,
            id = args!!.id
        )
        reminderViewModel.updateRemind(update)
        setAlarm(update)
    }

    companion object {
        const val ACTIVATE = true
    }
}

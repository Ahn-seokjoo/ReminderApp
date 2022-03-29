package com.delightroom.reminder.ui.setting

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.delightroom.reminder.R
import com.delightroom.reminder.base.BaseFragment
import com.delightroom.reminder.databinding.FragmentEditingReminderBinding
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.viewmodel.ReminderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderEditingFragment : BaseFragment<FragmentEditingReminderBinding>(R.layout.fragment_editing_reminder) {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val reminderViewModel: ReminderViewModel by activityViewModels()
    private val args by lazy { arguments?.getParcelable<ReminderData>("reminder") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditData()
        selectRingtone()
        setSelectedRingtoneData()
        clickSaveButton()
    }

    private fun setEditData() {
        args?.let { remindData ->
            binding.editNameRemind.setText(remindData.remind)
            binding.selectedRingtone.text = getRingtoneTitle(remindData.ringtone.toUri())
            binding.timePicker.hour = remindData.time.split(":")[0].toInt()
            binding.timePicker.minute = remindData.time.split(":")[1].toInt()
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

    private fun showAlertMessage() {
        AlertDialog.Builder(context)
            .setMessage("리마인더 이름은 반드시 포함되어야 합니다")
            .setPositiveButton("예") { _, _ ->
            }
            .create()
            .show()
    }

    private fun FragmentEditingReminderBinding.addRemind() {
        reminderViewModel.addRemind(
            ReminderData(
                time = getTimeResult(),
                remind = editNameRemind.text.toString(),
                activate = ACTIVATE,
                ringtone = reminderViewModel.ringtone.value
            )
        )
    }

    private fun FragmentEditingReminderBinding.updateRemind() {
        reminderViewModel.updateRemind(
            ReminderData(
                time = getTimeResult(),
                remind = editNameRemind.text.toString(),
                activate = ACTIVATE,
                ringtone = reminderViewModel.ringtone.value,
                id = args!!.id
            )
        )
    }

    private fun FragmentEditingReminderBinding.getTimeResult() = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

    companion object {
        const val ACTIVATE = true
    }
}

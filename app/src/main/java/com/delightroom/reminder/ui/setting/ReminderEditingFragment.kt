package com.delightroom.reminder.ui.setting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.delightroom.reminder.R
import com.delightroom.reminder.base.BaseFragment
import com.delightroom.reminder.databinding.FragmentEditingReminderBinding
import com.delightroom.reminder.viewmodel.RingtoneViewModel

class ReminderEditingFragment : BaseFragment<FragmentEditingReminderBinding>(R.layout.fragment_editing_reminder) {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val ringtoneViewModel: RingtoneViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectRingtone()
        setSelectedRingtoneData()
    }

    private fun selectRingtone() {
        binding.ringtoneList.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            getResult.launch(intent)
        }
    }

    private fun setSelectedRingtoneData() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                ringtoneViewModel.setRingtoneData(uri)
                binding.currentRingtone.text = RingtoneManager.getRingtone(context, uri).getTitle(context)
            }
        }
    }
}

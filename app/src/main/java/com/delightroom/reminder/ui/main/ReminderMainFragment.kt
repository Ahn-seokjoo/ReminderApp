package com.delightroom.reminder.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.delightroom.reminder.R
import com.delightroom.reminder.base.BaseFragment
import com.delightroom.reminder.databinding.FragmentMainReminderBinding
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.ui.main.recyclerview.ReminderRecyclerviewAdapter
import com.delightroom.reminder.ui.setting.ReminderEditingFragment

class ReminderMainFragment : BaseFragment<FragmentMainReminderBinding>(R.layout.fragment_main_reminder) {
    private val reminderAdapter = ReminderRecyclerviewAdapter(::startEditReminder)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.adapter = reminderAdapter

        binding.addReminder.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("MainReminder")
                replace<ReminderEditingFragment>(R.id.fragment_container_view)
            }
        }
    }

    private fun startEditReminder(reminder: ReminderData) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            arguments = bundleOf("reminder" to reminder)
            addToBackStack("MainReminder")
            replace<ReminderEditingFragment>(R.id.fragment_container_view, args = arguments)
        }
    }
}

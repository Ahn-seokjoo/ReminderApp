package com.delightroom.reminder.repository

import com.delightroom.reminder.repository.local.LocalRemindDataSource
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val local: LocalRemindDataSource) {
    suspend fun getAll() = local.getAll().sortedBy {
        it.time
    }

    suspend fun addRemind(reminderData: ReminderData): Long {
        return local.addRemind(reminderData)
    }

    suspend fun deleteRemind(reminderId: Int) {
        local.deleteRemind(reminderId)
    }

    suspend fun updateRemind(reminderData: ReminderData) {
        local.updateRemind(reminderData)
    }
}

package com.delightroom.reminder.repository.local

import com.delightroom.reminder.repository.ReminderData

interface LocalRemindDataSource {
    suspend fun getAll(): List<ReminderData>
    suspend fun addRemind(reminderData: ReminderData): Long
    suspend fun deleteRemind(reminderId: Int)
    suspend fun updateRemind(reminderData: ReminderData)
}

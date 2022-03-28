package com.delightroom.reminder.repository

interface LocalRemindDataSource {
    suspend fun getAll(): List<ReminderData>
    suspend fun addRemind(reminderData: ReminderData)
    suspend fun deleteRemind(reminderId: Int)
    suspend fun updateRemind(reminderData: ReminderData)
}

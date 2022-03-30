package com.delightroom.reminder.repository.local

import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.repository.room.ReminderDao
import javax.inject.Inject

class LocalRemindDataSourceImpl @Inject constructor(private val reminderDao: ReminderDao) : LocalRemindDataSource {
    override suspend fun getAll(): List<ReminderData> {
        return reminderDao.getAll()
    }

    override suspend fun addRemind(reminderData: ReminderData): Long {
        return reminderDao.addReminder(reminderData)
    }

    override suspend fun deleteRemind(reminderId: Int) {
        reminderDao.deleteReminder(reminderId)
    }

    override suspend fun updateRemind(reminderData: ReminderData) {
        reminderDao.updateReminder(reminderData)
    }

}

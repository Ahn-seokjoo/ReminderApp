package com.delightroom.reminder.repository.room

import androidx.room.*
import com.delightroom.reminder.repository.ReminderData

@Dao
interface ReminderDao {
    @Query("SELECT * FROM ReminderList")
    suspend fun getAll(): List<ReminderData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminderData: ReminderData): Long

    @Query("DELETE FROM ReminderList WHERE id = :reminderId")
    suspend fun deleteReminder(reminderId: Int)

    @Update
    suspend fun updateReminder(reminderData: ReminderData)

}

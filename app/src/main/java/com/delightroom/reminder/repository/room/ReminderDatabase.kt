package com.delightroom.reminder.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.delightroom.reminder.repository.ReminderData

@Database(entities = [ReminderData::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}

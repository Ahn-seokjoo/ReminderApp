package com.delightroom.reminder.util

import android.content.Context
import androidx.room.Room
import com.delightroom.reminder.repository.LocalRemindDataSource
import com.delightroom.reminder.repository.LocalRemindDataSourceImpl
import com.delightroom.reminder.repository.room.ReminderDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderModule {
    @Binds
    @Singleton
    abstract fun bindLocalRepository(local: LocalRemindDataSourceImpl): LocalRemindDataSource

    companion object {
        @Singleton
        @Provides
        fun providesRoom(@ApplicationContext context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ReminderDatabase::class.java,
                "ReminderData.db"
            ).build()

        @Singleton
        @Provides
        fun providesInstalledReminderDao(reminderDatabase: ReminderDatabase) =
            reminderDatabase.reminderDao()
    }
}

package com.delightroom.reminder.repository

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "ReminderList")
@Parcelize
data class ReminderData(
    val time: String,
    val remind: String,
    val activate: Boolean,
    val ringtone: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable




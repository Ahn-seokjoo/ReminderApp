package com.delightroom.reminder.repository

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "ReminderList", indices = [Index(value = arrayOf("time"), unique = true)])
@Parcelize
data class ReminderData(
    @ColumnInfo(name = "time")
    val time: String,
    val remind: String,
    var activate: Boolean,
    val ringtone: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable




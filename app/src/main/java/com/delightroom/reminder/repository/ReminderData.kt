package com.delightroom.reminder.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReminderData(
    val id: Int,
    val time: String,
    val remind: String,
    val activate: Boolean,
    val ringtone: String
) : Parcelable

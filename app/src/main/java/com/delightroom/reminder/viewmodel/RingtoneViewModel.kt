package com.delightroom.reminder.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RingtoneViewModel : ViewModel() {
    private val ringtone = MutableLiveData<String>()

    fun setRingtoneData(uri: Uri?) {
        ringtone.value = uri.toString()
    }
}

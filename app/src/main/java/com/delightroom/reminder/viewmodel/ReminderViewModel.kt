package com.delightroom.reminder.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delightroom.reminder.repository.LocalRemindDataSource
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.util.NonNullLiveData
import com.delightroom.reminder.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val local: LocalRemindDataSource) : ViewModel() {
    private val _ringtone = NonNullMutableLiveData(DEFAULT_RINGTONE)
    val ringtone: NonNullLiveData<String> = _ringtone

    private val _remind = NonNullMutableLiveData<List<ReminderData>>(emptyList())
    val remind: NonNullLiveData<List<ReminderData>> = _remind

    fun setRingtoneData(uri: Uri?) {
        _ringtone.value = uri.toString()
    }

    fun getAll() {
        viewModelScope.launch {
            _remind.value = local.getAll().sortedBy {
                it.time
            }
        }
    }

    fun addRemind(reminderData: ReminderData) {
        viewModelScope.launch {
            local.addRemind(reminderData)
            getAll()
        }
    }

    fun deleteRemind(reminderId: Int) {
        viewModelScope.launch {
            local.deleteRemind(reminderId)
            getAll()
        }
    }

    fun updateRemind(reminderData: ReminderData) {
        viewModelScope.launch {
            local.updateRemind(reminderData)
            getAll()
        }
    }

    companion object {
        const val DEFAULT_RINGTONE = "content://settings/system/ringtone"
    }
}

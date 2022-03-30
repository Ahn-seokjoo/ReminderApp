package com.delightroom.reminder.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delightroom.reminder.repository.ReminderData
import com.delightroom.reminder.repository.ReminderRepository
import com.delightroom.reminder.util.NonNullLiveData
import com.delightroom.reminder.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val repository: ReminderRepository) : ViewModel() {
    private val _ringtone = NonNullMutableLiveData(DEFAULT_RINGTONE)
    val ringtone: NonNullLiveData<String> = _ringtone

    private val _remind = NonNullMutableLiveData<List<ReminderData>>(emptyList())
    val remind: NonNullLiveData<List<ReminderData>> = _remind

    fun setRingtoneData(uri: Uri?) {
        _ringtone.value = uri.toString()
    }

    fun getAll() {
        viewModelScope.launch {
            _remind.value = repository.getAll()
        }
    }

    suspend fun addRemind(reminderData: ReminderData): Long {
        val id = viewModelScope.async {
            repository.addRemind(reminderData)
        }.await()
        getAll()
        return id
    }

    fun deleteRemind(reminderId: Int) {
        viewModelScope.launch {
            repository.deleteRemind(reminderId)
            getAll()
        }
    }

    fun updateRemind(reminderData: ReminderData) {
        viewModelScope.launch {
            repository.updateRemind(reminderData)
            getAll()
        }
    }

    companion object {
        const val DEFAULT_RINGTONE = "content://settings/system/ringtone"
    }
}

package com.elbaz.eliran.rappeltout.ui.viewmodels

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elbaz.eliran.rappeltout.receiver.AlarmReceiver
import com.elbaz.eliran.rappeltout.utils.singleArgViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eliran Elbaz on 18-Apr-20.
 */
class MainViewModel (private val app: Application) : AndroidViewModel(app) {



    companion object {
        /**
         * Factory for creating [MainViewModel]
         *
         * @param arg the repository to pass to [MainViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }


    // Set default date (when user don't select any date)
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/M/yyyy")
    private val formattedDate = sdf.format(Date())

    private val _selectedDate = MutableLiveData(formattedDate)

    val selectedDate: LiveData<String> = _selectedDate

    val selectedDateText : LiveData<String> = _selectedDate

    // Set the current date in LiveData
    fun onReminderAdd(date : String){
        _selectedDate.value = date
    }



}
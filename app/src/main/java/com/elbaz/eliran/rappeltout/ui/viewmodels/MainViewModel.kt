package com.elbaz.eliran.rappeltout.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elbaz.eliran.rappeltout.utils.singleArgViewModelFactory
import com.facebook.internal.Utility.logd
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

//    val selectedDateText : LiveData<Date> = _selectedDate

    // Set the current date in LiveData
    fun onReminderAdd(date : String){
        // Build a Date object
//        val date = GregorianCalendar(year, month - 1, dayOfMonth).time
//        setDateForReminder(date)
        _selectedDate.value = date
    }

//    fun setDateForReminder(date : String){
//        _selectedDate.value = date
//    }


}
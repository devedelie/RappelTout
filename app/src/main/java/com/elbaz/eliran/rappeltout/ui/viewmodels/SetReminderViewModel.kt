package com.elbaz.eliran.rappeltout.ui.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elbaz.eliran.rappeltout.receiver.AlarmReceiver
import com.elbaz.eliran.rappeltout.utils.singleArgViewModelFactory
import java.util.*

/**
 * Created by Eliran Elbaz on 18-Apr-20.
 */
class SetReminderViewModel (private val app: Application) : AndroidViewModel(app) {

    // For AlarmManager
    private val alarmMgr = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    companion object {
        /**
         * Factory for creating [SetReminderViewModel]
         *
         * @param arg the repository to pass to [SetReminderViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::SetReminderViewModel)
    }

    private val _date = MutableLiveData("date")

    val date : LiveData<String> = _date

    fun setDate(date : String){
        _date.value = date
    }



    /**
     *  Alarm creator
     */

    fun oneTimeAlarmIntent(isRepeating : Boolean, id : Int, day : Int, month : Int, year : Int, hour : Int, minutes : Int){
        if(!isRepeating){
            Log.d("AlarmIntent - One Time", "Received")
            // Create intent
            val alarmIntent = Intent(app, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(app, id, intent, 0)
            }
            // Set the alarm to start at Date&Time.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(2020, 3, 21, 10, minutes,0) // April: months (0-11)
            }

            alarmMgr?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }

    fun repeatingAlarmIntent(isRepeating : Boolean, id : Int){
        if(isRepeating){
            Log.d("AlarmIntent - Repeating", "Received")
            // Create intent
            val alarmIntent = Intent(app, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(app, id, intent, 0)
            }
            // Set the alarm to start at Date&Time.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(2020, 3, 21, 10, 52,0) // April: months (0-11)
            }
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                900000L,
                alarmIntent
            )
        }
    }

    fun cancelAlarmWithId(id : Int){
        val alarmIntent = Intent(app, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(app, id, intent, 0)
        }
        // If the alarm has been set, cancel it.
        alarmMgr?.cancel(alarmIntent)

    }

//    fun alarmDateInstance(day : Int, month : Int, year : Int, hour : Int, minutes : Int) : Calendar{
//        // Set the alarm to start at 8:30 a.m.
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.DAY_OF_MONTH, day)
//            set(Calendar.MONTH, month)
//            set(Calendar.YEAR, year)
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minutes)
//        }
//        return calendar
//    }



    enum class repeatTiming {
        INTERVAL_FIFTEEN_MINUTES,
        INTERVAL_HALF_HOUR,
        INTERVAL_HOUR,
        INTERVAL_HALF_DAY,
        INTERVAL_DAY,
    }

    fun test(){
        Toast.makeText(app, "OnReceive alarm test top", Toast.LENGTH_SHORT).show()
    }
}
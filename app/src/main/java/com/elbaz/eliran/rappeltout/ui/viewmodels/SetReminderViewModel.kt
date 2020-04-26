package com.elbaz.eliran.rappeltout.ui.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elbaz.eliran.rappeltout.receiver.AlarmReceiver
import com.elbaz.eliran.rappeltout.utils.Utils
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

    private val _startDate = MutableLiveData("date")
    private val _endDate = MutableLiveData("date")
    private val _startTime = MutableLiveData("HH:mm")
    private val _endTime = MutableLiveData("HH:mm")
    private val _beforeEvent = MutableLiveData(ValueBeforeEvent.DAYS_BEFORE.string)
    private val _valueBeforeEvent = MutableLiveData(1)

    val startDate : LiveData<String> = _startDate
    val endDate : LiveData<String> = _endDate
    val startTime : LiveData<String> = _startTime
    val endTime : LiveData<String> = _endTime
    val beforeEvent : LiveData<String> = _beforeEvent
    val valueBeforeEvent : LiveData<Int> = _valueBeforeEvent

    fun setStartDate(startDate : String){
        _startDate.value = startDate
    }

    fun setEndDate(endDate : String){
        _endDate.value = endDate
    }

    fun setStartTime(startTime : String){
        _startTime.value = startTime
//        verifyTiming()
    }

    fun setEndTime(endTime : String){
        _endTime.value = endTime
    }

    fun setBeforeEvent(beforeEvent : String){
        _beforeEvent.value = beforeEvent
    }

    fun setValueBeforeEvent(valueBeforeEvent : Int){
        _valueBeforeEvent.value = valueBeforeEvent
    }

    private fun toastMessage(message : String) = Toast.makeText(app, message, Toast.LENGTH_LONG).show()
    private fun verifyDates(dateStart : Date, dateEnd : Date) = dateEnd >= dateStart
    private fun verifyDateBig(dateStart : Date, dateEnd : Date) = dateEnd > dateStart
    private fun verifyTimes(timeStart : Date, timeEnd : Date) = timeEnd > timeStart
    // Functions to transform String to Date()
    private fun dateStart() = _startDate.value?.let { Utils.stringToDate(it) }
    private fun dateEnd() = _endDate.value?.let { Utils.stringToDate(it) }
    private fun timeStart() = _startTime.value?.let { Utils.stringToTime(it) }
    private fun timeEnd() = _endTime.value?.let { Utils.stringToTime(it) }


    fun setDates(receivedDate : Date, isStart : Boolean) {
        if(isStart){
            when (verifyDates(receivedDate, dateEnd()!!)) {
                true -> _startDate.value = Utils.dateToString(receivedDate)
                else -> toastMessage("End-Date cannot be earlier than Start-Date")
            }
        }else{ // end-date view
            when (verifyDates(dateStart()!! , receivedDate)) {
                true -> _endDate.value = Utils.dateToString(receivedDate)
                else -> toastMessage("End-Date cannot be earlier than Start-Date")
            }
        }
    }

    fun setTimes(receivedTime : Date , isStart : Boolean){
        // If end-date is bigger than start-Date, allow any hour selection
        if(verifyDateBig(dateStart()!!, dateEnd()!!)) {
            when(isStart){
                true -> _startTime.value = Utils.timeToString(receivedTime)
                else -> _endTime.value = Utils.timeToString(receivedTime)
            }
        }else{ // Else, (same day)  make sure that start-time isn't bigger than end-time
            if(isStart && verifyTimes(receivedTime, timeEnd()!!)){
                 _startTime.value = Utils.timeToString(receivedTime)
            }else if (!isStart && verifyTimes(timeStart()!!, receivedTime)){
                _endTime.value = Utils.timeToString(receivedTime)
            }else{
                toastMessage("Start-Time cannot be later than End-Time")
            }
        }
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



    enum class RepeatTiming {
        INTERVAL_FIFTEEN_MINUTES,
        INTERVAL_HALF_HOUR,
        INTERVAL_HOUR,
        INTERVAL_HALF_DAY,
        INTERVAL_DAY
    }

    enum class ValueBeforeEvent(val string: String){
        MINUTES_BEFORE("minute(s)"),
        HOURS_BEFORE("hour(s)"),
        DAYS_BEFORE("day(s)")
    }

}
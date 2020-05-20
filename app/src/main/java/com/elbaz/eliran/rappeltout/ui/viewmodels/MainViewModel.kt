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
import androidx.lifecycle.viewModelScope
import com.elbaz.eliran.rappeltout.data.db.ReminderRoomDB
import com.elbaz.eliran.rappeltout.data.repositories.ReminderRepository
import com.elbaz.eliran.rappeltout.model.Reminder
import com.elbaz.eliran.rappeltout.receiver.AlarmReceiver
import com.elbaz.eliran.rappeltout.utils.Utils
import com.elbaz.eliran.rappeltout.utils.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

/**
 * Created by Eliran Elbaz on 18-Apr-20.
 */
class MainViewModel (private val app: Application) : AndroidViewModel(app) {

    // Date initialization
    private var timeZone = DateTimeZone.forID("Europe/Paris")
    private var fullCurrentDate = DateTime.now(timeZone)

    /****************************
     * For Repository and DB
     ****************************/
    private val repository : ReminderRepository
    val allReminders: LiveData<List<Reminder>>

    init{
        val reminderDao = ReminderRoomDB.getDatabase(app, viewModelScope).reminderDao()
        repository = ReminderRepository(reminderDao)
        allReminders = repository.allReminders
    }

    //Launching a new coroutine to insert the data in a non-blocking way
    private fun insert(reminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reminder)
    }

    /****************************
     * For AlarmManager
     ****************************/
    private val alarmMgr = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /****************************
     * For Injection
     ****************************/
    companion object {
        //Factory for creating [MainViewModel]
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }

    /****************************
     * DATA
     ****************************/

    private lateinit var reminder : Reminder
    private var reminderPosition : Int = -1
    private val _isEditMode = MutableLiveData(false)

    // Configure MutableLiveDate with default values
    private var _title = MutableLiveData("")
    private var _selectedDate = MutableLiveData(fullCurrentDate) // As default: current date
    private var _startDate = MutableLiveData(fullCurrentDate)
    private var _startDateString = MutableLiveData(Utils.convertDateToString(fullCurrentDate))
    private var _endDate = MutableLiveData(fullCurrentDate.plusDays(1))
    private var _endDateString = MutableLiveData(Utils.convertDateToString(fullCurrentDate.plusDays(1)))
    private var _startTime = MutableLiveData(fullCurrentDate.plusHours(1))
    private var _startTimeString = MutableLiveData(Utils.convertTimeToString(fullCurrentDate.plusHours(1)))
    private var _endTime = MutableLiveData(fullCurrentDate.plusHours(2))
    private var _endTimeString = MutableLiveData(Utils.convertTimeToString(fullCurrentDate.plusHours(2)))
    private var _timeBeforeEvent = MutableLiveData(ValueBeforeEvent.MINUTES_BEFORE.string)
    private var _valueBeforeEvent = MutableLiveData(20)
    private var _eventColor = MutableLiveData(-1544140)
    private var _isRepeating = MutableLiveData(false)

    val title : LiveData<String> = _title
    val startDate : LiveData<DateTime> = _startDate
    val startDateString : LiveData<String> = _startDateString
    val endDate : LiveData<DateTime> = _endDate
    val endDateString : LiveData<String> = _endDateString
    val startTime : LiveData<DateTime> = _startTime
    val startTimeString : LiveData<String> = _startTimeString
    val endTime : LiveData<DateTime> = _endTime
    val endTimeString : LiveData<String> = _endTimeString
    val beforeEvent : LiveData<String> = _timeBeforeEvent
    val valueBeforeEvent : LiveData<Int> = _valueBeforeEvent
    val selectedDate: LiveData<DateTime> = _selectedDate
    val eventColor: LiveData<Int> = _eventColor
    val isRepeating : LiveData<Boolean> = _isRepeating
    val isEditMode : LiveData<Boolean> = _isEditMode


    fun onReminderAddClicked(date: DateTime?){
        _startDate.value = date
        _endDate.value = date?.plusDays(1)
        Utils.convertDateToString(date).let { _startDateString.value = it}
        Utils.convertDateToString(date?.plusDays(1)).let { _endDateString.value = it}
    }

    /****************************
     * Setters
     ****************************/

    fun setTitle(title : String){ _title.value = title }

    fun setStartDate(startDate : DateTime){
        _startDate.value = startDate
        Utils.convertDateToString(startDate).let { _startDateString.value = it}
    }

    fun setEndDate(endDate : DateTime){
        _endDate.value = endDate
        Utils.convertDateToString(endDate).let { _endDateString.value = it}
    }

    fun setStartTime(startTime : DateTime){
        _startTime.value = startTime
        Utils.convertTimeToString(startTime).let { _startTimeString.value = it}
    }

    fun setEndTime(endTime : DateTime){
        _endTime.value = endTime
        Utils.convertTimeToString(endTime).let { _endTimeString.value = it}
    }

    fun setTimeBeforeEvent(timeBeforeEvent : String){
        _timeBeforeEvent.value = timeBeforeEvent
    }

    fun setValueBeforeEvent(valueBeforeEvent : Int){
        _valueBeforeEvent.value = valueBeforeEvent
    }

    fun setEventColor(eventColor : Int){
        _eventColor.value = eventColor
    }

    fun setIsRepeating(isRepeating: Boolean){
        _isRepeating.value = isRepeating
    }

    fun setReminderToEdit(position : Int){
        reminderPosition = position
    }

    fun setIsEditMode(isEditMode : Boolean){
        Log.d("isEditMode1", "Received $isEditMode")
        _isEditMode.value = isEditMode
    }

    //Helper expressions
    private fun toastMessage(message : String) = Toast.makeText(app, message, Toast.LENGTH_LONG).show()
    private fun increaseDateDays(receivedDate: DateTime, value : Int) = receivedDate.plusDays(value)
    private fun increaseTimeHours(receivedTime: DateTime, value : Int) = receivedTime.plusHours(value)
    private fun decreaseDateDays(receivedDate: DateTime, value : Int) = receivedDate.minusDays(value)
    private fun decreaseDateMonths(receivedDate: DateTime, value : Int) = receivedDate.minusMonths(value)
    private fun decreaseDateYear(receivedDate: DateTime, value : Int) = receivedDate.minusYears(value)
    private fun decreaseTimeHours(receivedTime: DateTime, value : Int) = receivedTime.minusHours(value)
    private fun decreaseTimeMinutes(receivedTime: DateTime, value : Int) = receivedTime.minusMinutes(value)


    /****************************
     * Date & Time pickers
     ****************************/

    fun pickStartDate(receivedDate : DateTime){
        when (_endDate.value!!.toLocalDate() >= receivedDate.toLocalDate()) {
            true -> setStartDate(receivedDate) // to updates both values (String + dateTime)
            else -> handleDateValues(receivedDate)
        }
    }

    private fun handleDateValues(receivedDate: DateTime){
        setStartDate(receivedDate)
        setEndDate(increaseDateDays(receivedDate, 1))
    }

    fun pickEndDate(receivedDate : DateTime){
        println("Test XXX ${receivedDate.toLocalDate()} + ${_startDate.value!!.toLocalDate()}")
        when {
            receivedDate.toLocalDate() > _startDate.value!!.toLocalDate() -> {
                setEndDate(receivedDate) // updates both values (String + dateTime)
            }
            receivedDate.toLocalDate() == _startDate.value!!.toLocalDate() -> {
                setEndDate(receivedDate)
                handleTimeValues(_startTime.value!!) // Fix Dates if needed
            }
            else -> { toastMessage("End-Date cannot be earlier than Start-Date") }
        }
    }

    fun pickStartTime(receivedTime : DateTime){
        if(_endDate.value!! > _startDate.value!!){
            setStartTime(receivedTime)  // in this case, any hour can be selected
        }else{ // Dates are equal
            when(receivedTime < _endTime.value ){
                true -> setStartTime(receivedTime)
                else -> handleTimeValues(receivedTime)
            }
        }
    }

    fun pickEndTime(receivedTime: DateTime){
        if(_endDate.value!! > _startDate.value!!){
            setEndTime(receivedTime)  // in this case, any hour can be selected
        }else{ // Dates are equal
            when(_startTime.value!! < receivedTime ){
                true -> setEndTime(receivedTime)
                else -> toastMessage("End-Time cannot be earlier than Start-Time")
            }
        }
    }

    private fun handleTimeValues(receivedTime: DateTime){
        setStartTime(receivedTime)
        setEndTime(increaseTimeHours(receivedTime, 1))
    }

    fun updateUiVariablesForEditMode(){
        setTitle(allReminders.value?.get(reminderPosition)!!.title)
        // TODO: update all UI elements with data from current Reminder
    }


    fun resetToDefaultValues(){
        setTitle("")
//        _selectedDate = MutableLiveData(fullCurrentDate) // As default: current date
        setStartDate(fullCurrentDate)
        setEndDate(fullCurrentDate.plusDays(1))
        setStartTime(fullCurrentDate.plusHours(1))
        setEndTime(fullCurrentDate.plusHours(2))
        setTimeBeforeEvent(ValueBeforeEvent.MINUTES_BEFORE.string)
        setValueBeforeEvent(20)
        setEventColor(-1544140)
        setIsRepeating(false)
        setIsEditMode(false)
        setReminderToEdit(-1)
    }

    fun cancelAlarmBtnVisibility() : String{
        return when (_isEditMode.value!!){
            true -> "visible"
            else -> "gone"
        }

    }


    /****************************
     * Alarm Configurations
     ****************************/

    private fun oneTimeAlarmIntent(id : Int, day : Int, month : Int, year : Int, hour : Int, minutes : Int){
            Log.d("AlarmIntent - One Time", "Received")
            // Create intent
            val alarmIntent = Intent(app, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(app, id, intent, 0)
            }
            // Set the alarm to start at Date&Time.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(year, (month-1), day, hour, minutes,minutes) // *** Months (0-11)
            }

            alarmMgr?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
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

    /****************************
     * Execute Saving actions
     ****************************/

    fun manageSavingAction(receivedTitle : String, address : String){
        createObjectForReminder(receivedTitle, address) // Create Reminder object
        // Set a date value
        var fullEventDate = Utils.convertStringToDateTime(
            "${startDate.value!!.year}-" +
                    "${startDate.value!!.monthOfYear}-" +
                    "${startDate.value!!.dayOfMonth} " +
                    "${startTime.value!!.hourOfDay}:" +
                    "${startTime.value!!.minuteOfHour}")
        println("fullDate XXX ${fullEventDate.toString()}")

//         configure alarm time
        when (_timeBeforeEvent.value){
            "minute(s)" -> fullEventDate = fullEventDate!!.minusMinutes(_valueBeforeEvent.value!!)
            "hour(s)" -> fullEventDate = fullEventDate!!.minusHours(_valueBeforeEvent.value!!)
            "day(s)" -> fullEventDate = fullEventDate!!.minusDays(_valueBeforeEvent.value!!)
        }
        if(!isRepeating.value!!){ // Not repeating -> One time alert
            oneTimeAlarmIntent(
                1, // TODO: Solve ID - To be managed automatically
                fullEventDate!!.dayOfMonth,
                fullEventDate.monthOfYear,
                fullEventDate.year,
                fullEventDate.hourOfDay,
                fullEventDate.minuteOfHour)
            println("fullDate XXX2 ${fullEventDate.toString()}")
        }
    }

    private fun createObjectForReminder(receivedTitle : String, address : String){
        reminder = Reminder(receivedTitle)
        with(reminder){

            content = "content bb tt" // To change
            eventColor = _eventColor.value!!
            creationDate = fullCurrentDate.toString()
            startTime = _startTimeString.value
            endTime = _endTimeString.value
            startDate = _startDateString.value
            endDate = _endDateString.value
            alarmDate = "25/05/2020" // To change
            alarmTime = "10:00" // To change
            eventAddress = address
            repeatTimes = 3 // To change
            isRepeating = true // To change
            isActive = true // To change
        }
        insert(reminder)
    }

    /****************************
     * Helpers
     ****************************/

    // minute = 60sec * 1000millis
    // hour = minute * 60
    // day = hour * 24
    // week = day * 7

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

    enum class eventColors(val color : Int){
        YELLOW(0xFFD323),
        RED(0xFF0000),
        PURPLE(0x9900FF),
        BLUE(0x0000FF),
        LIGHT_BLUE(0x3D85C6),
        GREEN(0x00FF00),
        LIGHT_GREEN(0x6AA84F),
        ORANGE(0xE69138),
        LIGHT_ORANGE(0xF9CB9C),
        LIGHT_RED(0xE06666)
    }


}
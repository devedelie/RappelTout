package com.elbaz.eliran.rappeltout.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.ParseException
import android.util.Log
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eliran Elbaz on 17-Apr-20.
 */
object Utils {
    private var dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy")
    private var timeFormat = DateTimeFormat.forPattern("HH:mm")
    private var fullFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
    private var timeZone: DateTimeZone? = null
    private var fullCurrentDate :DateTime? = null

    fun initDateFormat() {
        if (timeZone != null) {
            return
        } else {
            timeZone = DateTimeZone.forID("Europe/Paris")
            fullCurrentDate = DateTime.now(timeZone) // Instead of DateTimeZone.UTC}
        }
    }

    /****************************
     * Network Availability
     ****************************/

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        Log.d(ContentValues.TAG, "isInternetAvailable: $activeNetworkInfo")
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    /****************************
     * Date Converters
     ****************************/

    fun subtractDate(dateTime: DateTime, timeValue: String, amountValue: Int): String{
            return when(timeValue){
                "minutes" -> dateTime.minusMinutes(amountValue).toString()
                "hours" -> dateTime.minusHours(amountValue).toString()
                "days" -> dateTime.minusDays(amountValue).toString()
                else -> "Error clamming date"
            }
        }

    fun addToDate(dateTime: DateTime, timeValue: String, amountValue: Int): String{
        return when(timeValue){
            "minutes" -> dateTime.plusMinutes(amountValue).toString()
            "hours" -> dateTime.plusHours(amountValue).toString()
            "days" -> dateTime.plusDays(amountValue).toString()
            else -> "Error clamming date"
        }
    }

    fun getCurrentDateTimeString(isDate: Boolean): String{
        return when(isDate){
            true -> fullCurrentDate?.toDateTime().toString()
            false -> fullCurrentDate?.toLocalTime().toString()
        }
    }

    fun getCurrentDateTime() : DateTime = fullCurrentDate!!.toDateTime()

    fun convertDateToString(value: DateTime?) : String = dateFormat.print(value)

    fun convertTimeToString(value : DateTime) : String = timeFormat.print(value)

    fun convertStringToDateTime(string: String): DateTime? = fullFormat.parseDateTime(string)

    fun convertStringToDate(string: String): DateTime? = dateFormat.parseDateTime(string)

    fun convertStringToTime(string: String): DateTime? = timeFormat.parseDateTime(string)

    fun formatDate(dateTime: DateTime): String = dateFormat.print(dateTime)

    fun formatTime(dateTime: DateTime): String = timeFormat.print(dateTime)


/////////-----------END----------------------

//    @SuppressLint("SimpleDateFormat")
//    fun getCurrentDateOrTimeString(isDate : Boolean) : String{
//        val sdf = SimpleDateFormat("dd/MM/yyyy")
//        val stf = SimpleDateFormat("HH:mm")
//        lateinit var formattedDate : String
//        return when(isDate){
//            true -> sdf.format(Date())
//            else -> stf.format(Date())
//        }
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun stringToDate(dateInString : String) : Date{
//        val formatter = SimpleDateFormat("dd/MM/yyyy")
//        lateinit var date : Date
//
//        try {
//            date  = formatter.parse(dateInString)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return date
//    }
//
//    // Set default date (when user don't select any date)
//    @SuppressLint("SimpleDateFormat")
//    fun dateToString(date : Date) : String{
//        var sdf = SimpleDateFormat("dd/MM/yyyy")
//        return sdf.format(date)
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun timeToString(time : Date) : String{
//        var stf = SimpleDateFormat("HH:mm")
//        return stf.format(time)
//    }
//
//
//    @SuppressLint("SimpleDateFormat")
//    fun stringToTime(dateInString : String) : Date{
//        var formatter = SimpleDateFormat("HH:mm")
//        lateinit var time : Date
//
//        try {
//            time  = formatter.parse(dateInString)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return time
//    }

}
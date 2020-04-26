package com.elbaz.eliran.rappeltout.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.ParseException
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eliran Elbaz on 17-Apr-20.
 */
object Utils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        Log.d(ContentValues.TAG, "isInternetAvailable: $activeNetworkInfo")
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateOrTimeString(isDate : Boolean) : String{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val stf = SimpleDateFormat("HH:mm")
        lateinit var formattedDate : String
        return when(isDate){
            true -> sdf.format(Date())
            else -> stf.format(Date())
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToDate(dateInString : String) : Date{
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        lateinit var date : Date

        try {
            date  = formatter.parse(dateInString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    // Set default date (when user don't select any date)
    @SuppressLint("SimpleDateFormat")
    fun dateToString(date : Date) : String{
        var sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun timeToString(time : Date) : String{
        var stf = SimpleDateFormat("HH:mm")
        return stf.format(time)
    }


    @SuppressLint("SimpleDateFormat")
    fun stringToTime(dateInString : String) : Date{
        var formatter = SimpleDateFormat("HH:mm")
        lateinit var time : Date

        try {
            time  = formatter.parse(dateInString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time
    }

}
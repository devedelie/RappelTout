package com.elbaz.eliran.rappeltout.utils

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

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

}
package com.elbaz.eliran.rappeltout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


/**
 * Created by Eliran Elbaz on 20-Apr-20.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("onReceive Alarm1", "Received")
        Toast.makeText(context, "OnReceive alarm test top", Toast.LENGTH_SHORT).show()

        // Re-create all the alarms in viewModel after the device reboots (fetch details from DB)
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Log.d("onReceive Alarm", "Received")
            Toast.makeText(context, "OnReceive alarm test", Toast.LENGTH_SHORT).show()
        }
    }
}
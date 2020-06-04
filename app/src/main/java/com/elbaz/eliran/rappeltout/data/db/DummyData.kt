package com.elbaz.eliran.rappeltout.data.db

import android.content.ContentValues
import com.elbaz.eliran.rappeltout.model.Reminder
import java.util.*

/**
 * Created by Eliran Elbaz on 01-May-20.
 */
internal object DummyData {

    fun reminderOne(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("id", 1)
        contentValues.put("title", "Title 1")
        contentValues.put("content", "Reminder 1 Content")
        contentValues.put("eventColor", -1544140)
        contentValues.put("creationDate", "02/05/2020")
        contentValues.put("startTime", "15:00")
        contentValues.put("endTime", "16:00")
        contentValues.put("startDate", "25/05/2020")
        contentValues.put("endDate", "25/05/2020")
        contentValues.put("alarmDate", "25/05/2020")
        contentValues.put("alarmTime", "10:00")
        contentValues.put("eventAddress", "Paris 75012")
        contentValues.put("repeatTimes", 1)
        contentValues.put("isRepeating", true)
        contentValues.put("isActive", true)

        return contentValues
    }

    fun reminderTwo(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("id", 2)
        contentValues.put("title", "Title 2")
        contentValues.put("content", "Reminder 2 Content")
        contentValues.put("eventColor", -1544140)
        contentValues.put("creationDate", "02/05/2020")
        contentValues.put("startTime", "17:00")
        contentValues.put("endTime", "19:00")
        contentValues.put("startDate", "25/05/2020")
        contentValues.put("endDate", "26/05/2020")
        contentValues.put("alarmDate", "25/05/2020")
        contentValues.put("alarmTime", "10:00")
        contentValues.put("eventAddress", "Paris 75015")
        contentValues.put("repeatTimes", 2)
        contentValues.put("isRepeating", true)
        contentValues.put("isActive", false)

        return contentValues
    }

}

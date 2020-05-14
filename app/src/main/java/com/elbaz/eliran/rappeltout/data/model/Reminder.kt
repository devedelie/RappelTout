package com.elbaz.eliran.rappeltout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Eliran Elbaz on 13-Apr-20.
 */
@Entity(tableName = "reminder_table")
class Reminder (var title : String){

    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
    var content : String? = null
    var eventColor : Int? = null
    var creationDate : String? = null
    var startTime : String? = null
    var endTime : String? = null
    var startDate : String? = null
    var endDate : String? = null
    var alarmDate : String? = null
    var alarmTime : String? = null
    var eventAddress : String? = null
    var repeatTimes : Int? = null
    var isRepeating : Boolean? = null
    var isActive : Boolean? = null

}
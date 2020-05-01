package com.elbaz.eliran.rappeltout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Eliran Elbaz on 13-Apr-20.
 */
@Entity(tableName = "reminder_table")
data class Reminder (@PrimaryKey(autoGenerate = true) val id : String,
                     val title : String,
                     val content : String,
                     val eventColor : Int,
                     val creationDate : String,
                     val alarmFullDate : String,
                     val eventAddress : String,
                     val repeatTimes : Int,
                     val isRepeating : Boolean,
                     val isActive : Boolean )
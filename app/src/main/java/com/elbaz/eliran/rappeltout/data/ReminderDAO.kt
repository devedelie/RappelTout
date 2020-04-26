package com.elbaz.eliran.rappeltout.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single

/**
 * Created by Eliran Elbaz on 23-Apr-20.
 */
interface ReminderDAO {
    @Query("SELECT * FROM reminders order by id DESC")
    fun getReminders(): Single<List<Reminder>>

    @Query("SELECT * FROM reminders where id = :id")
    fun getReminder(id: Int): Single<Reminder>

    @Insert
    fun insert(reminder: Reminder): Long

    @Delete
    fun delete(reminder: List<Reminder>)

    @Update
    fun update(reminder: Reminder): Int
}
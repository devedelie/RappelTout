package com.elbaz.eliran.rappeltout.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elbaz.eliran.rappeltout.model.Reminder
import io.reactivex.Single

/**
 * Created by Eliran Elbaz on 23-Apr-20.
 */
@Dao
interface ReminderDAO {
    @Query("SELECT * FROM reminder_table")
    fun getReminders(): LiveData<List<Reminder>>

//    @Query("SELECT * FROM reminder_table order by id DESC")
//    fun getReminders(): List<Reminder>

//    @Query("SELECT * FROM reminder_table where id = :id")
//    fun getReminder(id: Int): Single<Reminder>

    @Insert
    fun insert(reminder: Reminder): Long

    @Delete
    fun delete(reminder: List<Reminder>)

    @Update
    fun update(reminder: Reminder): Int
}
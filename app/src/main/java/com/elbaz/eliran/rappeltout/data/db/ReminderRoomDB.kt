package com.elbaz.eliran.rappeltout.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elbaz.eliran.rappeltout.model.Reminder
import com.elbaz.eliran.rappeltout.utils.Converters

/**
 * Created by Eliran Elbaz on 30-Apr-20.
 */
@Database(entities = arrayOf(Reminder::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class ReminderRoomDB : RoomDatabase() {

    abstract fun reminderDao() : ReminderDAO

    // --- INSTANCE ---
    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ReminderRoomDB? = null

        fun getDatabase(context : Context) : ReminderRoomDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderRoomDB::class.java,
                    "reminder_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
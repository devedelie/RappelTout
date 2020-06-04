package com.elbaz.eliran.rappeltout.data.db

import android.content.ContentValues
import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elbaz.eliran.rappeltout.model.Reminder
import com.elbaz.eliran.rappeltout.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Eliran Elbaz on 30-Apr-20.
 */
@Database(entities = arrayOf(Reminder::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReminderRoomDB : RoomDatabase() {

    abstract fun reminderDao() : ReminderDAO

    // --- INSTANCE ---
    companion object{
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ReminderRoomDB? = null

        fun getDatabase(
            context : Context,
            scope: CoroutineScope
        ) : ReminderRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderRoomDB::class.java,
                    "reminder_database"
                )
                    .addCallback(ReminderDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    //--------------------
    // CallBack
    //---------------------

    private class ReminderDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.reminderDao(), db)
                }
            }
        }

        suspend fun populateDatabase(reminderDao: ReminderDAO, @NonNull db : SupportSQLiteDatabase) {
            // Delete all content.
//            reminderDao.deleteAll()

            val contentValues = ContentValues()

            db.insert("reminder_table", OnConflictStrategy.IGNORE, DummyData.reminderOne())
            db.insert("reminder_table", OnConflictStrategy.IGNORE, DummyData.reminderTwo())

        }
    }

}

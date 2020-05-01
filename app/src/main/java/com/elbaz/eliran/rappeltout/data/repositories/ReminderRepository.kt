package com.elbaz.eliran.rappeltout.data.repositories

import androidx.lifecycle.LiveData
import com.elbaz.eliran.rappeltout.data.db.ReminderDAO
import com.elbaz.eliran.rappeltout.model.Reminder

/**
 * Created by Eliran Elbaz on 01-May-20.
 */
class ReminderRepository (private val reminderDao: ReminderDAO) {

        // Room executes all queries on a separate thread.
        // Observed LiveData will notify the observer when the data has changed.
        val allReminders: LiveData<List<Reminder>> = reminderDao.getReminders()

        suspend fun insert(reminder: Reminder) {
            reminderDao.insert(reminder)
        }
}
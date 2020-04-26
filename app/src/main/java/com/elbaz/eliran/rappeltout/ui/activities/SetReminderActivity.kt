package com.elbaz.eliran.rappeltout.ui.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.ActivitySetReminderBinding
import com.elbaz.eliran.rappeltout.ui.viewmodels.SetReminderViewModel
import com.elbaz.eliran.rappeltout.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel
import kotlinx.android.synthetic.main.activity_set_reminder.*
import java.text.SimpleDateFormat
import java.util.*


class SetReminderActivity : AppCompatActivity() {

    private val cal = Calendar.getInstance()

    private val viewModel by lazy { ViewModelProvider(this).get(SetReminderViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBinding
        val binding : ActivitySetReminderBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_set_reminder)
        binding.lifecycleOwner = this
        binding.setReminderViewModel = viewModel

        val receivedDate = intent.getStringExtra("Date") // Get date from intent
        viewModel.setStartDate(receivedDate) // Set dates in viewModel
        viewModel.setEndDate(receivedDate)
        viewModel.setStartTime(Utils.getCurrentDateOrTimeString(false))
        viewModel.setEndTime(Utils.getCurrentDateOrTimeString(false))

       configureButtons()
    }

    //----------------
    // Configurations
    //----------------

    private fun configureButtons() {
        val backBtn : ImageView = findViewById(R.id.activity_back_button)
        backBtn.setOnClickListener{
            finish()
        }
        val saveBtn : TextView = findViewById(R.id.activity_save_button)
        saveBtn.setOnClickListener{
//            viewModel.saveReminderAction()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @SuppressLint("SimpleDateFormat")
    fun onTimeClicked(view: View){  // TimePicker Spinner-Style is defined in Styles.xml
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//            cal.set(Calendar.HOUR_OF_DAY, hour)
//            cal.set(Calendar.MINUTE, minute)
//            val time = SimpleDateFormat("HH:mm").format(cal.time)
            when (view) {
                start_time -> viewModel.setTimes(Utils.stringToTime("$hour:$minute"), true)
                end_time -> viewModel.setTimes(Utils.stringToTime("$hour:$minute"), false) // TODO: Try to add +1 hour when setting the start time
            }
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun onDateClicked (view : View){
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            var toDate = Utils.stringToDate("$dayOfMonth/${(monthOfYear+1)}/$year")
            when(view){
                start_date -> viewModel.setDates(toDate, true)
                end_date -> viewModel.setDates(toDate, false)
            }
        }, year, month, day)
        dpd.show()
    }

    fun defineAlertTimeBeforeEvent(view: View){
        val singleItems = arrayOf("Minutes", "Hours", "Days")
        var checkedItem = 1
        MaterialAlertDialogBuilder(this)
            .setIcon(android.R.drawable.ic_lock_idle_alarm)
            .setTitle("Before Event")
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                checkedItem = which
                when(which){
                    0 -> setValueBeforeEvent("minute(s)")
                    1 -> setValueBeforeEvent("hour(s)")
                    2 -> setValueBeforeEvent("day(s)")
                }
            }
            .setPositiveButton(
                android.R.string.yes) { dialog, which ->
                    // action
                }
            .setNegativeButton(android.R.string.no, null) // null listener - no action.
            .show()
    }

    private fun setValueBeforeEvent(value : String) = viewModel.setBeforeEvent(value)


}


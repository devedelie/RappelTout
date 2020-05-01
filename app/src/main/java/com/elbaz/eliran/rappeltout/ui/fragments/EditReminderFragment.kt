package com.elbaz.eliran.rappeltout.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.FragmentEditReminderBinding
import com.elbaz.eliran.rappeltout.events.BackBtnPressEvent
import com.elbaz.eliran.rappeltout.model.Reminder
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.elbaz.eliran.rappeltout.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.back_btn_bar.*
import kotlinx.android.synthetic.main.fragment_edit_reminder.*
import org.greenrobot.eventbus.EventBus
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditReminderFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val cal = Calendar.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentEditReminderBinding>(inflater,
            R.layout.fragment_edit_reminder,container,false)

        // Obtain ViewModel from ViewModelProviders
        viewModel = activity ?. run {
            ViewModelProvider(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setStartTime(Utils.getCurrentDateOrTimeString(false))
        viewModel.setEndTime(Utils.getCurrentDateOrTimeString(false))

        // Event Color (by default or from DB)
//        viewModel.eventColor.value?.let { event_color.setCardBackgroundColor(it) }
        // Observe color changes
//        viewModel.eventColor.observe(requireActivity(), androidx.lifecycle.Observer {color ->
//            println("XXX color change $color")
//            event_color.setCardBackgroundColor(color)
//        })

        configureUIListeners()
    }

    private fun configureUIListeners(){
        start_date.setOnClickListener{ v -> onDateClicked(v) }
        end_date.setOnClickListener{ v -> onDateClicked(v) }
        start_time.setOnClickListener{ v -> onTimeClicked(v) }
        end_time.setOnClickListener{ v -> onTimeClicked(v) }
        set_time_before_event.setOnClickListener{ v -> defineAlertTimeBeforeEvent(v) }
        event_color.setOnClickListener{ v -> defineEventColor(v) }
        back_button.setOnClickListener{v -> onBackBtnClicked(v)}
        save_button.setOnClickListener{v -> onSaveBtnClicked(v)}
    }

    private fun onBackBtnClicked(view: View){
        EventBus.getDefault().post(BackBtnPressEvent())
    }

    private fun onSaveBtnClicked(view: View){
        var reminder3 = Reminder(
            "Reminder3",
            "Reminder3 Content",
            -1544140,
            "02/05/2020",
            "15:00",
            "16:00",
            "25/05/2020",
            "25/05/2020",
            "25/05/2020",
            "10:00",
            "Paris 75012",
            4,
            true,
            true)
        viewModel.insert(reminder3)
        EventBus.getDefault().post(BackBtnPressEvent()) // Event to close the fragment
    }

    private fun onDateClicked (view : View){
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            var toDate = Utils.stringToDate("$dayOfMonth/${(monthOfYear+1)}/$year")
            when(view){
                start_date -> viewModel.setDates(toDate, true)
                end_date -> viewModel.setDates(toDate, false)
            }
        }, year, month, day)
        dpd.show()
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
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun defineAlertTimeBeforeEvent(view: View){
        val singleItems = arrayOf("Minutes", "Hours", "Days")
        var checkedItem = 1
        MaterialAlertDialogBuilder(requireContext())
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


    private fun defineEventColor(view: View){
        val colorPicker = ColorPicker(requireActivity())
        colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
            override fun onChooseColor(position: Int, color: Int) {
                viewModel.setEventColor(color)
            }
            override fun onCancel() {
                // put code
            }
        })
//            .addListenerButton("newButton") { v, position, color -> { put code }
//            .disableDefaultButtons(true)
            .setDefaultColorButton(Color.parseColor("#f84c44"))
            .setColumns(5)
            .show()
    }
}

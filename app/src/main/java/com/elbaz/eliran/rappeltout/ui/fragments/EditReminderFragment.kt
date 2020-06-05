package com.elbaz.eliran.rappeltout.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.transition.MaterialContainerTransform
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
    private var isEditMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform() // Transaction animation
    }

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

        // Verify Fragment mode and execute action (Edit / Create)
        viewModel.isEditMode.observe(viewLifecycleOwner, androidx.lifecycle.Observer { isEdit ->
            // Update the cached copy of the reminders in the adapter
            updateUiForEditMode(isEdit)
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.setStartTime(Utils.getCurrentDateOrTimeString(false))
//        viewModel.setEndTime(Utils.getCurrentDateOrTimeString(false))

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

    /****************************
     * ACTIONS
     ****************************/

    private fun onBackBtnClicked(view: View){
        EventBus.getDefault().post(BackBtnPressEvent())
    }

    private fun onSaveBtnClicked(view: View){
        var address = if(event_address.text.toString().isNotEmpty())  "" else event_address.text.toString()
        if(title_edit_text.text.toString().isNotEmpty()){
            viewModel.manageSavingAction(title_edit_text.text.toString(), address)
            EventBus.getDefault().post(BackBtnPressEvent()) // Event to close the fragment
        }else{
            Toast.makeText(requireActivity(), "Please add a title", Toast.LENGTH_LONG).show()
        }
    }

    private fun onDateClicked (view : View){
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            val date = Utils.convertStringToDate("$dayOfMonth/${(monthOfYear+1)}/$year")
            when(view){
                start_date -> viewModel.pickStartDate(date!!)
                end_date -> viewModel.pickEndDate(date!!)
            }
        }, year, month, day)
        dpd.show()
    }

    @SuppressLint("SimpleDateFormat")
    fun onTimeClicked(view: View){  // TimePicker Spinner-Style is defined in Styles.xml
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            val time = Utils.convertStringToTime("$hour:$minute")
            when (view) {
                start_time -> viewModel.pickStartTime(time!!)
                end_time -> viewModel.pickEndTime(time!!)
            }
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun defineAlertTimeBeforeEvent(view: View){
        val singleItems = arrayOf("Minutes", "Hours", "Days")
        var checkedItem = -1 // non selected item
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

    private fun setValueBeforeEvent(value : String) = viewModel.setTimeBeforeEvent(value)


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

    private fun updateUiForEditMode(isEditMode: Boolean){
        Log.d("isEditMode2", "Received $isEditMode")
        if(isEditMode){ // Then update UI elements from DB
            // TODO: Update ViewModel LiveData's with details from requested Reminder model,
            //  the DataBinding will update the View automatically...
            viewModel.updateUiVariablesForEditMode()
        }
    }


    override fun onDetach() {
        super.onDetach()
        Log.d("XXXCHECK onDetach", "Received")
        viewModel.resetToDefaultValues()
    }
}

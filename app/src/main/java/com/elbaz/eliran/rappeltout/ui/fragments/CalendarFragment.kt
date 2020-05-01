package com.elbaz.eliran.rappeltout.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.FragmentCalendarBinding
import com.elbaz.eliran.rappeltout.ui.activities.MainActivity
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_edit_reminder.*

class CalendarFragment : Fragment() {

//    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(inflater,
            R.layout.fragment_calendar,container,false)

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

        calendarConfig(view)
    }


    @SuppressLint("SimpleDateFormat")
    fun calendarConfig(view: View){
        val calendarView = view.findViewById<CalendarView>(R.id.calendar)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // months are indexed from 0. So, 0 means January, 1 means february etc.
            val date = "$dayOfMonth/${(month + 1)}/$year"
//            viewModel.onReminderAdd(dayOfMonth, month+1, year)
            viewModel.onReminderAdd(date)
        }
    }
}

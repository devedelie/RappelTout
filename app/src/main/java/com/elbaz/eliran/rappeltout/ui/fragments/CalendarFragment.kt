package com.elbaz.eliran.rappeltout.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.FragmentCalendarBinding
import com.elbaz.eliran.rappeltout.events.EditFragmentEvent
import com.elbaz.eliran.rappeltout.ui.adapters.ReminderListAdapter
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.elbaz.eliran.rappeltout.utils.ItemClickSupport
import com.elbaz.eliran.rappeltout.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.greenrobot.eventbus.EventBus


class CalendarFragment : Fragment() {

//    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var viewModel: MainViewModel
    lateinit var fab: FloatingActionButton

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
        recyclerViewConfig()
        configureFab()
        configureOnClickRecyclerView()
        setDatabaseObserver()
    }

    private fun configureFab(){
        fab = requireView().findViewById(R.id.floatingBtn) as FloatingActionButton
        fab.setOnClickListener {
            EventBus.getDefault().post(EditFragmentEvent()) // Event to inflate the fragment
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun calendarConfig(view: View){
        val calendarView = view.findViewById<CalendarView>(R.id.calendar)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // months are indexed from 0. So, 0 means January, 1 means february etc.
            val date = "$dayOfMonth/${(month + 1)}/$year"
            Utils.convertStringToDate(date)?.let { viewModel.onReminderAddClicked(it) }
        }
    }

    private fun recyclerViewConfig(){
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerview)
        // OR recyclerView.***  ?
        val adapter = ReminderListAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // fun setDatabaseObserver()
        viewModel.allReminders.observe(requireActivity(), Observer { reminders ->
            // Update the cached copy of the reminders in the adapter
            reminders?.let { adapter.setReminder(it) }
        })
    }

    //  Configure item click on RecyclerView
    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerview, R.layout.fragment_calendar)
            .setOnItemClickListener { recyclerView, position, v ->
                // RecyclerView onClick action
                println("XXX onCLick ${viewModel.allReminders.value!![position].title}")
                viewModel.setReminderToEdit(position) // Set the current position in viewModel
                // TODO : Load the fragment with the current reminder data from database
                viewModel.setIsEditMode(true) // Set Fragment to EditMode
                EventBus.getDefault().post(EditFragmentEvent()) // Event to inflate the fragment
            }
    }

    private fun setDatabaseObserver(){

    }
}

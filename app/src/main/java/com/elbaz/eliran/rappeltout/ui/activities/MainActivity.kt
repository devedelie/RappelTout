package com.elbaz.eliran.rappeltout.ui.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.ActivityMainBinding
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    // Obtain ViewModel from ViewModelProviders
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        // DataBinding
        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        setSupportActionBar(toolbar)


//        floatingBtnConfig()
        calendarConfig()
        start("second")
    }


//    fun floatingBtnConfig(){
//        floatingBtn.setOnClickListener { view ->
//            Snackbar.make(view, "Floating button action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }

    fun onClickAddReminder(view: View){
        val intent = Intent(this@MainActivity, SetReminderActivity::class.java)
        intent.putExtra("Date", viewModel.selectedDate.value.toString())
        startActivity(intent)
    }

    @SuppressLint("SimpleDateFormat")
    fun calendarConfig(){
        val calendarView = findViewById<CalendarView>(R.id.calendar)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val date = "$dayOfMonth/${(month + 1)}/$year"
//            viewModel.onReminderAdd(dayOfMonth, month+1, year)
            viewModel.onReminderAdd(date)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }



    /////////////////////////////////////
    // Try to refactor all non-UI methods into viewModel class to keep UI logic only
    // using DataBinding

    fun logout(item: MenuItem){
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show()
                clearActivityBackStack()
            }
    }

    private fun clearActivityBackStack() {
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    //////////////// TODO: Test
    private val instructions : Map<String, () -> Unit> = mapOf(
        "first" to {Toast.makeText(this, "First", Toast.LENGTH_LONG).show()},
        "second" to {Toast.makeText(this, "Second", Toast.LENGTH_LONG).show()}
    )

    var someMethod: (() -> Unit)? = null

    fun start(word : String){
        someMethod?.invoke()
        instructions[word]?.invoke()
    }

    /////////////// TODO: End of Test

}
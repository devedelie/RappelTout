package com.elbaz.eliran.rappeltout.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.internal.bind.ArrayTypeAdapter.FACTORY
import com.google.gson.internal.bind.DateTypeAdapter.FACTORY
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
    // Obtain ViewModel from ViewModelProviders
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        // DataBinding
        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        setSupportActionBar(toolbar)

        speakConfig()
//        floatingBtnConfig()
        calendarConfig()
    }

    fun speakConfig(){
        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

//    fun floatingBtnConfig(){
//        floatingBtn.setOnClickListener { view ->
//            Snackbar.make(view, "Floating button action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }

    fun onClickAddReminder(view: View){
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
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

    override fun onInit(status: Int){

        if (status == TextToSpeech.SUCCESS) {
            // set language for tts
            val result = tts!!.setLanguage(Locale.FRANCE)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

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


}
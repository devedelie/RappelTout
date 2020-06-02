package com.elbaz.eliran.rappeltout.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.elbaz.eliran.rappeltout.R

import kotlinx.android.synthetic.main.activity_display_reminder.*
import java.util.*

class DisplayReminderActivity : AppCompatActivity() , TextToSpeech.OnInitListener{

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
//    private val viewModel by lazy { ViewModelProviders.of(this).get(ReminderViewModel::class.java) }



    // TODO : TEST ACTIVITY ******



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_reminder)

        speakConfig()
    }

    private fun speakConfig(){
        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

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
}

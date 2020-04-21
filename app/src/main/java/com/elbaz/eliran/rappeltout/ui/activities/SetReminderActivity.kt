package com.elbaz.eliran.rappeltout.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.ActivityMainBinding
import com.elbaz.eliran.rappeltout.databinding.ActivitySetReminderBinding
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.elbaz.eliran.rappeltout.ui.viewmodels.SetReminderViewModel

class SetReminderActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SetReminderViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBinding
        val binding : ActivitySetReminderBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_set_reminder)
        binding.lifecycleOwner = this
        binding.setReminderViewModel = viewModel

        val receivedDate = intent.getStringExtra("Date")
        viewModel.setDate(receivedDate)

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

}

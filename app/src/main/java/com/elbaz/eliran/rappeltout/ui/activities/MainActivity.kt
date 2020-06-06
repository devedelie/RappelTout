package com.elbaz.eliran.rappeltout.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.ActivityMainBinding
import com.elbaz.eliran.rappeltout.events.BackBtnPressEvent
import com.elbaz.eliran.rappeltout.events.EditFragmentEvent
import com.elbaz.eliran.rappeltout.ui.fragments.CalendarFragment
import com.elbaz.eliran.rappeltout.ui.fragments.EditReminderFragment
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.elbaz.eliran.rappeltout.utils.Utils
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

    // Obtain ViewModel from ViewModelProviders
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var binding : ActivityMainBinding

    // Fragments
    private val calendarFragment = CalendarFragment()
    private val editReminderFragment = EditReminderFragment()

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

        Utils.initDateFormat() // Initialize Format
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBinding
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        setSupportActionBar(toolbar)

        // Load calendar fragment as default
        supportFragmentManager.beginTransaction()
            .add(R.id.host_fragment, calendarFragment)
            .commit()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBackBtnEvent(event: BackBtnPressEvent?) { /* Do something */
        println("XXX event happened")
        loadFragment(calendarFragment)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEditFragmentMode(event: EditFragmentEvent?) { /* Do something */
        println("XXX editReminderFragment event happened")
        loadFragment(editReminderFragment)
    }


    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addSharedElement(floatingBtn, "shared_element_container") // apply transaction between shared elements
        fragmentTransaction.replace(R.id.host_fragment, fragment, fragment.tag)
        fragmentTransaction.addToBackStack(fragment.tag)
        fragmentTransaction.commit()
        // Show/Hide view elements
        if (fragment == editReminderFragment){
            supportActionBar!!.hide()
        }else if(fragment == calendarFragment){
            supportActionBar!!.show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun invokePrivacyActivity(item: MenuItem){
        val intent = Intent (this@MainActivity, PrivacyPolicyActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

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

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


}
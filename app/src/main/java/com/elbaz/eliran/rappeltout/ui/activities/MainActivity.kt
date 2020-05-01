package com.elbaz.eliran.rappeltout.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.databinding.ActivityMainBinding
import com.elbaz.eliran.rappeltout.events.BackBtnPressEvent
import com.elbaz.eliran.rappeltout.ui.fragments.CalendarFragment
import com.elbaz.eliran.rappeltout.ui.fragments.EditReminderFragment
import com.elbaz.eliran.rappeltout.ui.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import io.opencensus.trace.MessageEvent
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // DataBinding
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

//        setSupportActionBar(toolbar)

        // Load calendar fragment as default
        supportFragmentManager.beginTransaction()
            .add(R.id.host_fragment, calendarFragment)
            .commit()

//        start("second")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBackBtnEvent(event: BackBtnPressEvent?) { /* Do something */
        println("XXX event happened")
        loadFragment(calendarFragment)
    }

    fun onFloatingBtnClicked(view: View){
            loadFragment(editReminderFragment)
    }

    private fun loadFragment(fragment: Fragment){
        when (fragment){
            editReminderFragment -> binding.floatingBtn.hide()
            calendarFragment -> binding.floatingBtn.show()
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.host_fragment, fragment)
        fragmentTransaction.commit()
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

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
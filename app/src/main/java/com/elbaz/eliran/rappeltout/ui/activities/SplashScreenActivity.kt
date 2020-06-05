package com.elbaz.eliran.rappeltout.ui.activities

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.danlew.android.joda.JodaTimeAndroid

open class SplashScreenActivity : AppCompatActivity() {
    lateinit var moon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        moon = findViewById(R.id.moon)

        JodaTimeAndroid.init(this)  // Initialize JudaTime
    }

    override fun onResume() {
        super.onResume()
        // Verify Network connectivity
        if (!Utils.isInternetAvailable(this)) {
            displayMobileDataSettingsDialog(this, this)
        } else {
            CoroutineScope(Main).launch {
                animateMoon()
                fakeNetworkRequest() // Fake network delay to show loading animation
                when (isCurrentUserLogged()){
                    true -> intentActivity(MainActivity::class.java)
                    else -> intentActivity(LoginActivity::class.java)  // Go to Login screen if logged Off
                }
            }
        }
    }

    private suspend fun fakeNetworkRequest(){
        delay(1000)
    }

    private fun animateMoon(){
        val animator = ObjectAnimator.ofFloat(moon, View.TRANSLATION_X, 300f)
        animator.start()
    }

    // --------------------
    // UTILS
    // --------------------
    private fun isCurrentUserLogged(): Boolean? {
        return this.getCurrentUser() != null
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    open fun intentActivity(classname: Class<*>) {
        val intent = Intent(this, classname)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    // --------------------
    // NETWORK CONNECTIVITY
    // --------------------
    private fun displayMobileDataSettingsDialog(activity: Activity?, context: Context): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet")
        builder.setMessage("Please enable network access")
        builder.setPositiveButton("Continue") {
                dialog, which -> finish()
            val intent = Intent(context, this::class.java)
            context.startActivity(intent)
            dialog.cancel()
        }
        builder.show()
        return builder.create()
    }
}

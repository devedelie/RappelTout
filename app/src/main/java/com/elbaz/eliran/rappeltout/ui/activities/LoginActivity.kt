package com.elbaz.eliran.rappeltout.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.elbaz.eliran.rappeltout.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var buttonsContainer: LinearLayout
    // For Data
    companion object {
        private const val RC_SIGN_IN: Int = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        coordinatorLayout = findViewById(R.id.login_coordinator_layout)

        buttonsContainer = findViewById(R.id.provider_login_all_login_buttons_layout)
    }

    //--------------------
    // ACTIONS
    //--------------------

    fun emailSignIn(view: View) {
    // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(
                    arrayListOf(AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.fui_ic_anonymous_white_24dp)
                .setIsSmartLockEnabled(false) //Disable SmartLock to enable Espresso testing on UI**
                .build(),
            RC_SIGN_IN)
    }

    fun gmailSignIn(view: View) {
        // Create and launch sign-in intent
//        Toast.makeText(this, "This action was not implemented", Toast.LENGTH_LONG).show()
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(
                    arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.fui_ic_anonymous_white_24dp)
                .build(),
            RC_SIGN_IN)
    }

    fun facebookSignIn(view: View) {
        // Create and launch sign-in intent
        Toast.makeText(this, "This action was not implemented", Toast.LENGTH_LONG).show()
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setTheme(R.style.LoginTheme)
//                .setAvailableProviders(
//                    arrayListOf(AuthUI.IdpConfig.FacebookBuilder().build()))
//                .setIsSmartLockEnabled(false, true)
//                .setLogo(R.drawable.fui_ic_anonymous_white_24dp)
//                .build(),
//            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                Log.d(ContentValues.TAG, "Login test XXX" )
                checkUser()
            } else {
                // ERRORS
                when {
                    response == null -> {
                        showSnackBar(coordinatorLayout, getString(R.string.error_authentication_canceled))
                    }
                    response.error!!.errorCode == ErrorCodes.NO_NETWORK -> {
                        showSnackBar(coordinatorLayout, getString(R.string.error_no_internet))
                    }
                    response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR -> {
                        showSnackBar(coordinatorLayout, getString(R.string.error_unknown_error))
                    }
                }
            }
        }
    }

    private fun checkUser(){
        val user = FirebaseAuth.getInstance().currentUser
        showSnackBar(coordinatorLayout, getString(R.string.connection_succeed))
        val intent = Intent (this@LoginActivity, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun invokePrivacyPolicy(view: View) {
        val intent = Intent (this@LoginActivity, PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

    // --------------------
    // UI & Animations
    // --------------------

    // Show Snack Bar with a message
    private fun showSnackBar(coordinatorLayout: CoordinatorLayout, message: String) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}

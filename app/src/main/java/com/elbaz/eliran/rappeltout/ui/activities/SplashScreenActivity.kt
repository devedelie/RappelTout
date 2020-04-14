package com.elbaz.eliran.rappeltout.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elbaz.eliran.rappeltout.R
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        intentLogin()
    }

    private fun intentLogin(){
//        waitForDelay()
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        startActivity(intent)
    }

//    private suspend fun waitForDelay(){
//        delay(500)
//    }
}

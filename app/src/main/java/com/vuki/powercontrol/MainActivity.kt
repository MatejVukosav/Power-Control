package com.vuki.powercontrol

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast

/**
 * https://www.andreasschrade.com/2015/02/16/android-tutorial-how-to-create-a-kiosk-mode-in-android/
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Keep the screen on and bright while this kiosk activity is running.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setUpAdmin()
    }

    /**
     * Catch any focus change and dismiss any dialogs. Does not get called on short power press.
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) { // Close every kind of system dialog
            val closeDialog = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            sendBroadcast(closeDialog)
            Toast.makeText(this, "Changed focus", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.name
    }
}

package com.vuki.powercontrol

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by mvukosav on 11,January,2020
 */
class OnScreenOffReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) { // Detect short power button click
        if (Intent.ACTION_SCREEN_OFF == intent.action) {
            val ctx = context.applicationContext as App
            wakeUpDevice(ctx)
        }
    }

    @SuppressLint("WakelockTimeout")
    private fun wakeUpDevice(context: App) {
        val wakeLock = context.wakeLock // get WakeLock reference via AppContext
        if (wakeLock.isHeld) {
            wakeLock.release() // release old wake lock
        }
        // create a new wake lock...
        wakeLock.acquire()
        // ... and release again
        wakeLock.release()
    }
}
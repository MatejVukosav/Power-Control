package com.vuki.powercontrol

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by mvukosav on 12,January,2020
 */
open class BaseActivity : AppCompatActivity() {

    private lateinit var decorView: View
    private lateinit var dpm: DevicePolicyManager
    lateinit var deviceAdmin: ComponentName

    /**
     * @return A newly instantiated [android.content.ComponentName] for this DeviceAdminReceiver.
     */
    open fun getComponentName(context: Context): ComponentName {
        return ComponentName(context.applicationContext, AdminReceiver::class.java)
    }

    protected fun setUpAdmin() {
        deviceAdmin = getComponentName(this)
        if (!App.isInLockMode()) {

            dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

            dpm.also {
                if (!it.isAdminActive(deviceAdmin)) {
                    Log.e(KIOSK_MODE_ERROR_TAG, getString(R.string.not_device_admin))
                }

                if (it.isDeviceOwnerApp(packageName)) {
                    it.setLockTaskPackages(deviceAdmin, arrayOf(packageName))
                } else {
                    Log.e(KIOSK_MODE_ERROR_TAG, getString(R.string.not_device_owner))
                }
            }

            enableKioskMode(true)

            //TODO : for clear device Owner
//        } else {
//            mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//            mDpm.clearDeviceOwnerApp(getPackageName());

            // setRestrictions()
        }
        decorView = window.decorView
        hideSystemUI()
    }

    private fun hideSystemUI() {
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun enableKioskMode(enabled: Boolean) {
        try {
            if (enabled) {
                if (dpm.isLockTaskPermitted(this.packageName)) {
                    App.setIsInLockMode(true)
                    startLockTask()
                } else {
                    App.setIsInLockMode(false)
                    Log.e(KIOSK_MODE_ERROR_TAG, getString(R.string.kiosk_not_permitted))
                }
            } else {
                App.setIsInLockMode(false)
                stopLockTask()
            }
        } catch (e: Exception) {
            App.setIsInLockMode(false)
            // TODO: Log and handle appropriately
            e.localizedMessage?.apply {
                Log.e(KIOSK_MODE_ERROR_TAG, this)
            }
        }
    }

    companion object {
        const val KIOSK_MODE_ERROR_TAG = "Kiosk Mode Error"
    }

}
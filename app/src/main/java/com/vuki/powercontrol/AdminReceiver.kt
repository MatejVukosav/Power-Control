package com.vuki.powercontrol

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.os.UserManager
import android.widget.Toast

/**
 * Created by mvukosav on 12,January,2020
 */
class AdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(
            context,
            context.getString(R.string.device_admin_enabled),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return context.getString(R.string.device_admin_warning)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(
            context,
            context.getString(R.string.device_admin_disabled),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_enabled), Toast.LENGTH_SHORT)
            .show()

        val dpm = getManager(context)
        val admin = getWho(context)

        if (dpm.isDeviceOwnerApp(admin.packageName)) {
            arrayOf(
                UserManager.DISALLOW_FACTORY_RESET,
                UserManager.DISALLOW_SAFE_BOOT,
                UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA,
                UserManager.DISALLOW_ADJUST_VOLUME,
                UserManager.DISALLOW_ADD_USER
            ).forEach { dpm.addUserRestriction(admin, it) }
        }
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_disabled), Toast.LENGTH_SHORT)
            .show()
    }
}
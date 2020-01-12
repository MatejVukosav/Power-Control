package com.vuki.powercontrol;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.view.WindowManager;

/**
 * Created by mvukosav on 12,January,2020
 * <p>
 * Useful links:
 * <p>
 * https://www.andreasschrade.com/2015/02/16/android-tutorial-how-to-create-a-kiosk-mode-in-android/
 * https://github.com/android/enterprise-samples
 * https://developer.android.com/work/dpc/dedicated-devices/cookbook
 */
public class App extends Application {

    public static boolean isInLockMode;
    private PowerManager.WakeLock wakeLock;
    private OnScreenOffReceiver onScreenOffReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerKioskModeScreenOffReceiver();

        /*
        try {
            //execute through terminal as runtime didn't work during tests
            Runtime.getRuntime().exec("dpm set-device-owner com.vuki.powercontrol/.AdminReceiver");
            Runtime.getRuntime().exec("dpm set-profile-owner com.vuki.powercontrol/.AdminReceiver Matej");
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }

    private void registerKioskModeScreenOffReceiver() {
        // register screen off receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        onScreenOffReceiver = new OnScreenOffReceiver();
        registerReceiver(onScreenOffReceiver, filter);
    }

    public PowerManager.WakeLock getWakeLock() {
        if (wakeLock == null) {
            // lazy loading: first call, create wakeLock via PowerManager.
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.vuki.powercontrol:wakeup");
        }
        return wakeLock;
    }


    public static boolean isInLockMode() {
        return isInLockMode;
    }

    public static void setIsInLockMode(boolean isInLockMode) {
        App.isInLockMode = isInLockMode;
    }

}

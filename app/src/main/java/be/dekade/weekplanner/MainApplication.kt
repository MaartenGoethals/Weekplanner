package be.dekade.weekplanner

import android.app.AlarmManager
import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import be.dekade.weekplanner.helpers.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    init {
        val broadcastReceiver = NotificationHelper()
        val filter = IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED).apply {
            addAction(Intent.ACTION_BOOT_COMPLETED)
        }
        registerReceiver(broadcastReceiver, filter)
    }
}
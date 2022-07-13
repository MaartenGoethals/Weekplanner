package be.dekade.weekplanner.helpers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import be.dekade.weekplanner.R

const val NOTIFICATIONID = 1
const val CHANNELID = "Channel1"
const val titleExtra = "Titel"
const val messageExtra = "Message"

class MyNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val myNotification = NotificationCompat.Builder(context, CHANNELID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra)).build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATIONID, myNotification)
    }
}
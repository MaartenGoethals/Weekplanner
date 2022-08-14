package be.dekade.weekplanner.helpers

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import be.dekade.weekplanner.R
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensDag
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensWeek
import be.dekade.weekplanner.data.DagGegevens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

const val NOTIFICATIONID = 1
const val CHANNELID = "WEEKPLANNER_NOTIFICATION_CHANNEL"
const val titleExtra = "TitleExtra"
const val messageExtra = "MessageExtra"

@AndroidEntryPoint
class NotificationHelper : BroadcastReceiver() {
    @Inject
    lateinit var repository: ActiviteitEnDagGegevensRepository

    override fun onReceive(context: Context, intent: Intent) = runBlocking {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") { // all alarms are gone  in case of reboot
            val alarms = repository.getAlarms()
            for (alarm in alarms) {
                setNotification(alarm, context)
            }
        } else if (intent.action == "android.intent.action.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED") { // all alarms are gone if permission was revoked, so reschedule when permission is granted
            val alarms = repository.getAlarms()
            for (alarm in alarms) {
                setNotification(alarm, context)
            }
        } else if (intent.action?.startsWith(CHANNELID) == true) { //alarms scheduled in this application have a unique intent action starting with the channelid
            createNotificationChannel(context)
            val myNotification = NotificationCompat.Builder(context, CHANNELID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra)).build()
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATIONID, myNotification) //show notification
        }
    }

    companion object {
        @JvmStatic
        fun setNotifications(
            activiteitEnDagGegevensWeek: ActiviteitEnDagGegevensWeek,
            context: Context
        ) {
            if (activiteitEnDagGegevensWeek.activiteit.isNotificatieAan) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                for (dagGegeven: DagGegevens in activiteitEnDagGegevensWeek.dagGegevens) {
                    if (dagGegeven.isActief) {
                        val intent = Intent(context, AlarmReceiver::class.java)
                        val title = activiteitEnDagGegevensWeek.activiteit.titel
                        val message = activiteitEnDagGegevensWeek.activiteit.notities
                        intent.setAction(CHANNELID + dagGegeven.gegevensId.toString()) //alarms scheduled in this application have a unique intent action starting with the channelid
                        intent.putExtra(titleExtra, title)
                        intent.putExtra(messageExtra, message)

                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            NOTIFICATIONID,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        val time = Calendar.getInstance()
                        time.timeInMillis = System.currentTimeMillis()
                        time.set(Calendar.DAY_OF_WEEK, dagGegeven.dag)
                        time.set(
                            Calendar.HOUR_OF_DAY,
                            activiteitEnDagGegevensWeek.activiteit.startuur
                        )
                        time.set(
                            Calendar.MINUTE,
                            activiteitEnDagGegevensWeek.activiteit.startminuut
                        )
                        time.set(
                            Calendar.SECOND,
                            0
                        )
                        time.set(
                            Calendar.MILLISECOND,
                            0
                        )
                        if (time.timeInMillis < System.currentTimeMillis())
                            time.roll(Calendar.DATE, 7)
                        //TODO: vind oplossing voor uitgestelde activiteit.
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            (time.timeInMillis),
                            pendingIntent
                        )
                        Log.v("log","notification set on" + time.toString())
                    }
                }
            }
        }

        @JvmStatic
        fun setNotification(
            dagGegeven: ActiviteitEnDagGegevensDag,
            context: Context
        ) {
            if (dagGegeven.activiteit.isNotificatieAan) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (dagGegeven.dagGegevens.isActief) {
                    val intent = Intent(context, NotificationHelper::class.java)
                    val title = dagGegeven.activiteit.titel
                    val message = dagGegeven.activiteit.notities
                    intent.setAction(CHANNELID + dagGegeven.dagGegevens.gegevensId.toString()) //alarms scheduled in this application have a unique intent action starting with the channelid
                    intent.putExtra(titleExtra, title)
                    intent.putExtra(messageExtra, message)

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        NOTIFICATIONID,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    val time = Calendar.getInstance()
                    time.timeInMillis = System.currentTimeMillis()
                    time.set(Calendar.DAY_OF_WEEK, dagGegeven.dagGegevens.dag)
                    time.set(
                        Calendar.HOUR_OF_DAY,
                        dagGegeven.activiteit.startuur
                    )
                    time.set(
                        Calendar.MINUTE,
                        dagGegeven.activiteit.startminuut
                    )
                    time.set(
                        Calendar.SECOND,
                        0
                    )
                    time.set(
                        Calendar.MILLISECOND,
                        0
                    )
                    if (time.timeInMillis < System.currentTimeMillis())
                        time.roll(Calendar.DATE, 7)
                    //TODO: vind oplossing voor uitgestelde activiteit.
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        (time.timeInMillis),
                        pendingIntent
                    )
                }
            }
        }

        @JvmStatic
        fun turnNotificationsOff(
            context: Context,
            activiteitEnDagGegevensWeek: ActiviteitEnDagGegevensWeek
        ) {
            val intent = Intent(context, Notification::class.java)
            val title = activiteitEnDagGegevensWeek.activiteit.titel
            val message = activiteitEnDagGegevensWeek.activiteit.notities
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATIONID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }


        @JvmStatic
        fun createNotificationChannel(context: Context) {
            val name = "Alarm"
            val desc = "Alarm details"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNELID, name, importance)
            channel.description = desc
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
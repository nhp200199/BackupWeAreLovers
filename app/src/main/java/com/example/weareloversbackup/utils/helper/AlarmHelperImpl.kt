package com.example.weareloversbackup.utils.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.example.weareloversbackup.common.receivers.CoupleDateReceiver
import com.example.weareloversbackup.common.receivers.SystemBootReceiver
import java.util.Calendar
import javax.inject.Inject

class AlarmHelperImpl @Inject constructor(): IAlarmHelper {
    override fun scheduleCoupleAlarm(context: Context, forceInexact: Boolean): Int {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 9
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        val scheduleResult =  scheduleAlarm(context, calendar, forceInexact)
        if (scheduleResult == 1) {
            persistAlarmWhenDeviceReboot(context)
        }
        return scheduleResult
    }

    private fun persistAlarmWhenDeviceReboot(context: Context) {
        val receiver = ComponentName(context, SystemBootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun scheduleAlarm(context: Context, timestamps: Long, forceInexact: Boolean): Int {
        val intent = Intent(context, CoupleDateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (forceInexact) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timestamps,
                pendingIntent
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timestamps,
                    pendingIntent
                )
            } else {
                return 0
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timestamps,
                pendingIntent
            )
        }
        return 1
    }

    private fun scheduleAlarm(context: Context, calendar: Calendar, forceInexact: Boolean): Int {
        return scheduleAlarm(context, calendar.timeInMillis, forceInexact)
    }
}
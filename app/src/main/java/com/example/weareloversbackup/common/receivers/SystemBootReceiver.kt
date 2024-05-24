package com.example.weareloversbackup.common.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.weareloversbackup.utils.helper.IAlarmHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SystemBootReceiver @Inject constructor() : BroadcastReceiver() {
    @Inject lateinit var alarmHelper: IAlarmHelper
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SystemBootReceiver", "onReceive: ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val scheduleResult = alarmHelper.scheduleCoupleAlarm(context)
            if (scheduleResult == 0) {
                alarmHelper.scheduleCoupleAlarm(context, forceInexact = true)
            }
        }
    }

}
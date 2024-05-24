package com.example.weareloversbackup.common.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weareloversbackup.R
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import com.example.weareloversbackup.ui.MainActivity
import com.example.weareloversbackup.ui.MyApplication
import com.example.weareloversbackup.utils.helper.IAlarmHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class CoupleDateReceiver @Inject constructor() : BroadcastReceiver() {
    @Inject lateinit var alarmHelper: IAlarmHelper
    @Inject lateinit var coupleRepository: ICoupleRepository
    private val titles = arrayOf(
        "Chú ý chú ý!!!",
        "Hello, bạn biết gì chưa?"
    )
    private val contents = arrayOf(
        "Tèn ten, thêm 1 tháng là thêm tình cảm nồng nàn nhé!",
        "Chúc mừng chúc mừng, tình cảm lứa đôi mãnh liệt như 2 bạn thật đáng nể"
    )

      override fun onReceive(context: Context, intent: Intent) {
          val scheduleCoupleAlarm = alarmHelper.scheduleCoupleAlarm(context)
          if (scheduleCoupleAlarm == 0) {
              alarmHelper.scheduleCoupleAlarm(context, forceInexact = true)
          }

          checkShowNotification(context)
    }

    private fun checkShowNotification(context: Context) {
        val calendar = Calendar.getInstance()
        val currentDay = calendar[Calendar.DAY_OF_MONTH]
        val currentMonth = calendar[Calendar.MONTH]

        val coupleDateTimestamps = coupleRepository.getCoupleDateTimestamps()
        val dateObject: Date = Date(coupleDateTimestamps)
        val coupleDay = dateObject.date
        val coupleMonth = dateObject.month

        Log.i("CoupleDateService", "current date: $currentDay-${currentMonth + 1}")
        Log.i("CoupleDateService", "couple date: $coupleDay-${coupleMonth + 1}")
        if (currentDay == coupleDay - 1 && currentMonth != coupleMonth) {
            //TODO: notify on exact date and different notification when current month = couple month (1 year)
            showNotification(context, testing = false)
        } else {
            showNotification(context, testing = true)
        }
    }

    private fun showNotification(context: Context, testing: Boolean) {
        val intentActivity = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentActivity,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(
            context,
            MyApplication.CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getRandomString(titles))
            .setContentText(if (testing) "testing" else getRandomString(contents))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(0, builder.build())
        }
    }

    private fun getRandomString(listOfStrings: Array<String>): String {
        val random = Random()
        return listOfStrings[random.nextInt(listOfStrings.size)]
    }
}
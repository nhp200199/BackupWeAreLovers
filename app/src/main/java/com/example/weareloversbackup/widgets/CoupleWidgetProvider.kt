package com.example.weareloversbackup.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.RemoteViews
import com.example.weareloversbackup.R
import com.example.weareloversbackup.data.constant.PREF_COUPLE_DATE
import com.example.weareloversbackup.data.constant.SHARE_PREF_USER_INFO
import com.example.weareloversbackup.ui.MainActivity
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CoupleWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val view = RemoteViews(
                context!!.packageName,
                R.layout.widget_couple_text
            ).apply {
                setOnClickPendingIntent(R.id.fr_widget_container, pendingIntent)
                updateCoupleDaysText(context, this)
            }

            appWidgetManager?.updateAppWidget(appWidgetId, view)
        }
    }

    private fun updateCoupleDaysText(context: Context, remoteViews: RemoteViews) {
        val sharedPreferences = context.getSharedPreferences(SHARE_PREF_USER_INFO, Context.MODE_PRIVATE)
        val coupleDateTimestamps = sharedPreferences.getLong(PREF_COUPLE_DATE, 0L)
        val coupleDaysCount = getCoupleDaysCount(coupleDateTimestamps)

        remoteViews.setTextViewText(R.id.tv_couple_days, "$coupleDaysCount Days")
    }

    private fun getCoupleDaysCount(coupleDate: Long): Long {
        val current = Calendar.getInstance().timeInMillis
        val diffTimestamps = current - coupleDate
        return TimeUnit.DAYS.convert(diffTimestamps, TimeUnit.MILLISECONDS)
    }
}
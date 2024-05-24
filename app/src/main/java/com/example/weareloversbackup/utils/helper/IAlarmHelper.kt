package com.example.weareloversbackup.utils.helper

import android.content.Context

interface IAlarmHelper {
    fun scheduleCoupleAlarm(context: Context, forceInexact: Boolean = false): Int
}
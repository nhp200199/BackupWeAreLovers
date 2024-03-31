package com.example.weareloversbackup.utils

import com.example.weareloversbackup.data.constant.DEFAULT_DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

fun formatDate(timestamps: Long, pattern: String = DEFAULT_DATE_FORMAT): String {
    if (timestamps.toInt() == 0) return ""

    val simpleDateFormat = SimpleDateFormat(pattern)
    return simpleDateFormat.format(Date(timestamps))
}

fun parseDateTimestamps(date: String, pattern: String = DEFAULT_DATE_FORMAT): Long {
    if (date.isNullOrBlank()) return 0L

    val simpleDateFormat = SimpleDateFormat(pattern)
    return try {
        simpleDateFormat.parse(date).time
    } catch (e: ParseException) {
        0L
    }
}
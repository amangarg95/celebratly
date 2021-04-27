package com.kiprosh.optimizeprime.helper

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    fun changeDateFormat(inputDate: String): String {
        return try {
            val date1: Date = SimpleDateFormat("yyyy-MM-dd").parse(inputDate)
            val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            outputDateFormat.format(date1)
        } catch (e: Exception) {
            "-"
        }
    }

    fun getDifferenceInDate(startDate: Date, endDate: Date): Triple<Long, Long, Long> {
        var different = endDate.time - startDate.time
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return Triple(elapsedDays, elapsedHours, elapsedMinutes)
    }

    fun getDateFromString(date: String): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)!!
    }

}
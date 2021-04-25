package com.kiprosh.optimizeprime.helper

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    fun changeDateFormat(inputDate: String): String {
        return try {
            val date1: Date = SimpleDateFormat("yyyy-MM-dd").parse(inputDate)
            val outputDateFormat = SimpleDateFormat("dd, MMM yyyy", Locale.getDefault())
            outputDateFormat.format(date1)
        } catch (e: Exception) {
            "-"
        }
    }
}
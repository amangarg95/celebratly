package com.kiprosh.optimizeprime.helper

import com.example.optimizeprimeandroidapp.model.OccurrencesResponse

class FeedHelper {
    fun sortList(feedsList: ArrayList<OccurrencesResponse>): List<OccurrencesResponse> {
        val dateTimeUtil = DateTimeUtil()
        feedsList.forEach {
            val date = dateTimeUtil.getDateFromString(it.startAt)
            it.timestamp = date.time
        }
        return feedsList.sortedByDescending { it.timestamp }
    }
}
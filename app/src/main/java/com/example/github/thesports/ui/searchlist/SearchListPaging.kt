package com.example.github.thesports.ui.searchlist

import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.http.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *   Created by Lee Zhang on 10/26/20 01:54
 **/

class SearchListRepository{

    suspend fun getEventData(strEvent: String): List<LeagueEvent> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.getSearchEvent(strEvent).event
    }

}
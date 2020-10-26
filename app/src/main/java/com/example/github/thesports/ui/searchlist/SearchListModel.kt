package com.example.github.thesports.ui.searchlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.thesports.base.safeLaunch
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.ui.home.HomeListViewRepository
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/26/20 01:55
 **/
class SearchListModel(private val response: SearchListRepository)  : ViewModel(){

    var searchEventLists= MutableLiveData<List<LeagueEvent>>()

    fun  fatchSearchEvent(string: String){
        viewModelScope.safeLaunch {
            block = {
//                LogUtils.error(response.getEventData(string).toString())
                searchEventLists.value = response.getEventData(string)
            }

            onError ={
                LogUtils.error(it.stackTraceToString())
            }
        }
    }
}
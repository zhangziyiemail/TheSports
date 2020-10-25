package com.example.github.thesports.ui.eventdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.thesports.base.safeLaunch
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.entity.LeagueWithEvent
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/24/20 16:31
 **/
class EventDetailViewModel(private val response: EventDetailViewRepository) : ViewModel() {
    var eventDetail = MutableLiveData<EventDetail>()

    fun fatchEventDetail(id: Long) {
        viewModelScope.safeLaunch {
            block ={
                eventDetail.value = response.getEventDetailData(id)
            }
            onError ={
                LogUtils.error(it.toString())
            }
        }

    }

}
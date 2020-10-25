package com.example.github.thesports.ui.eventdetail

import androidx.databinding.ViewDataBinding
import com.example.github.thesports.base.BaseRecyclerAdapter
import com.example.github.thesports.base.BaseViewHolder
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.http.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *   Created by Lee Zhang on 10/24/20 16:30
 **/

class EventDetailViewRepository{
    suspend fun getEventDetailData(id:Long): EventDetail = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.getlookupevent(id).events[0]
    }
}


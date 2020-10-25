package com.example.github.thesports.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.ui.home.HomeListViewRepository
import com.example.github.thesports.ui.home.HomeViewModel

/**
 *   Created by Lee Zhang on 10/24/20 16:30
 **/

class EventDetaiModeFactory (private val repository: EventDetailViewRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventDetailViewModel(repository) as T
    }
}
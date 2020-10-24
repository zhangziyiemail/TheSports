package com.example.github.thesports.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.ui.select.SelectListViewRepository
import com.example.github.thesports.ui.select.SelectViewModel

/**
 *   Created by Lee Zhang on 10/22/20 17:21
 **/

class HomeViewModeFactory (private val repository: HomeListViewRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
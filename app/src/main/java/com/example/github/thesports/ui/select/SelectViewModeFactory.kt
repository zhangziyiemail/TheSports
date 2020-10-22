package com.example.github.thesports.ui.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *   Created by Lee Zhang on 10/19/20 22:56
 **/
class SelectViewModeFactory(private val repository: SelectListViewRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SelectViewModel(repository) as T
    }
}
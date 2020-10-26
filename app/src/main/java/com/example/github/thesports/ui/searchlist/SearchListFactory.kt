package com.example.github.thesports.ui.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.ui.home.HomeListViewRepository
import com.example.github.thesports.ui.home.HomeViewModel

class SearchListFactory  (private val repository: SearchListRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchListModel(repository) as T
    }
}
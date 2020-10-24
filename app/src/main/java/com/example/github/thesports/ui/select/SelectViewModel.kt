package com.example.github.thesports.ui.select

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.thesports.base.safeLaunch
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.entity.SportWithLeagueList
import com.example.github.thesports.utils.LogUtils


/**
 *   Created by Lee Zhang on 10/20/20 13:59
 **/
class SelectViewModel(private val response: SelectListViewRepository) : ViewModel() {
    var sportWithLeague = MutableLiveData<List<SportWithLeagueList>>()
    fun fatchSproutAndLeague() {
        viewModelScope.safeLaunch {
            block = {
                if (MyDatabaseUtils.SportDao.getSportWithLeague().isEmpty()) {

                    val allSpourts = response.getAllSports()
                    MyDatabaseUtils.SportDao.insertSportList(allSpourts)

                    val allLeagues = response.getAllLeagues()
                    MyDatabaseUtils.leagueDao.insertLeagueList(allLeagues)
                }
                sportWithLeague.value = MyDatabaseUtils.SportDao.getSportWithLeague()
            }
            onError = {
                LogUtils.error(it.stackTraceToString())
            }
        }
    }
}
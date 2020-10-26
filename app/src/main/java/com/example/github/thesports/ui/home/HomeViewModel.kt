package com.example.github.thesports.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.thesports.base.safeLaunch
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.entity.LeagueWithEvent
import com.example.github.thesports.utils.LogUtils
import com.example.github.thesports.widget.CustomDialog
import java.text.SimpleDateFormat

class HomeViewModel(private val response: HomeListViewRepository) : ViewModel() {
    var leagueWithEventList = MutableLiveData<List<LeagueWithEvent>>()
    var leagueEventLists = MutableLiveData<List<LeagueEvent>>()

    //    List<FollowSportWithLeagueList>
    fun fatchLeagueEventList() {
        viewModelScope.safeLaunch {
            block = {
                val leagueList = MyDatabaseUtils.leagueDao.getFollowLeagueList(true)
                val lastEventData = response.getNextEventData(leagueList[0].idLeague)
                MyDatabaseUtils.leagueEventDao.insertLeagueList(lastEventData)
                val nextEventData = response.getLastEventData(leagueList[0].idLeague)
                MyDatabaseUtils.leagueEventDao.insertLeagueList(nextEventData)
                leagueWithEventList.value =
                    MyDatabaseUtils.leagueEventDao.getFollowLeagueWithEvent(true)
            }
            onError = {
                LogUtils.error(it.stackTraceToString())
            }
        }
    }

    fun fatchLeagueEventWhithId(id: Int) {
        viewModelScope.safeLaunch {
            block = {
                val leagueList = MyDatabaseUtils.leagueDao.getFollowLeagueList(true)
                var alllest = mutableListOf<LeagueEvent>()
                if (MyDatabaseUtils.leagueEventDao.getLeagueList(leagueList[id].strLeague)
                        .isEmpty()
                ) {
                    val lastEventData = response.getNextEventData(leagueList[id].idLeague)
                    MyDatabaseUtils.leagueEventDao.insertLeagueList(lastEventData)
                    val nextEventData = response.getLastEventData(leagueList[id].idLeague)
                    MyDatabaseUtils.leagueEventDao.insertLeagueList(nextEventData)
                    alllest.addAll(lastEventData)
                    alllest.addAll(nextEventData)
                    leagueEventLists.value = alllest
                } else {
                    leagueEventLists.value =
                        MyDatabaseUtils.leagueEventDao.getLeagueList(leagueList[id].strLeague)
                }
            }
        }
    }

    fun fatchEndLeagueEventWhithId(id: Int,mark :String) {
        viewModelScope.safeLaunch {

            block = {
                val leagueList = MyDatabaseUtils.leagueDao.getFollowLeagueList(true)
                leagueEventLists.value = MyDatabaseUtils.leagueEventDao.getEndedLeagueList(leagueList[id].strLeague,mark)
            }
        }
    }

    fun fatchNoStartLeagueEventWhithId(id: Int,mark :String) {
        viewModelScope.safeLaunch {

            block = {
                val leagueList = MyDatabaseUtils.leagueDao.getFollowLeagueList(true)
                leagueEventLists.value = MyDatabaseUtils.leagueEventDao.getEndedLeagueList(leagueList[id].strLeague,mark)
            }
        }
    }

    fun fatchSortData(){

        leagueEventLists.value = leagueEventLists?.let {
            it.value?.sortedBy {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(it.dateEvent+" "+it.strTime).time
            }
        }
    }

    fun fatchLeagueEventWhithIdFromInternet(id: Int){

        var alllest = mutableListOf<LeagueEvent>()
        viewModelScope.safeLaunch {
            block = {
                val leagueList = MyDatabaseUtils.leagueDao.getFollowLeagueList(true)
                val lastEventData = response.getNextEventData(leagueList[id].idLeague)
                MyDatabaseUtils.leagueEventDao.insertLeagueList(lastEventData)
                val nextEventData = response.getLastEventData(leagueList[id].idLeague)
                MyDatabaseUtils.leagueEventDao.insertLeagueList(nextEventData)
                alllest.addAll(lastEventData)
                alllest.addAll(nextEventData)
                leagueEventLists.value = alllest

            }
            onError = {
                LogUtils.error(it.message)
            }
        }
    }


    var likeSearchList = MutableLiveData<List<String>>()

    fun fatchLikeSearchView(strevent: String){
        viewModelScope.safeLaunch {
            block ={
                likeSearchList.value  = response.getEventData(strevent)
            }
        }
    }


}


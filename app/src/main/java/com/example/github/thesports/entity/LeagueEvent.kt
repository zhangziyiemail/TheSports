package com.example.github.thesports.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *   Created by Lee Zhang on 10/22/20 19:19
 */

data class LeagueEventData(var events: MutableList<LeagueEvent>)

data class LeagueEData(var event: MutableList<LeagueEvent>)
@Parcelize
@Entity(tableName = "LeagueEvent")
data class LeagueEvent (
    @PrimaryKey
    val idEvent : String,
    val strEvent : String?,
    val strFilename:String?,
    val strSport : String?,
    val idLeague :String?,
    val strLeague : String?,
    val strSeason : String?,
    val strHomeTeam : String?,
    val strAwayTeam: String?,
    val strTimestamp : String?,
    val dateEvent : String?,
    val strTime: String?,
    val strVenue : String?,
    val strCountry : String?,
    val strStatus :String?,
    val strPostponed :String?,
    val strLocked : String?,
    val strThumb : String?,
    val intHomeScore : String?,
    val intAwayScore : String?,
    val idHomeTeam : String?,
    val idAwayTeam :String?,
    val noti :Boolean = false
) : Parcelable
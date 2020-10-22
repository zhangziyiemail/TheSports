package com.example.github.thesports.entity

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 *   Created by Lee Zhang on 10/19/20 23:37
 **/

data class SportData(var sports : MutableList<Sport>)


data class LeagueData(var leagues : MutableList<League>)

@Parcelize
@Entity
data class Sport(
    val idSport: Long = 0,
    @PrimaryKey @ColumnInfo(name = "strSport") val strSport: String,
    val strFormat: String,
    val strSportThumb: String,
    val strSportThumbGreen: String,
    val strSportDescription: String,
) : Parcelable

@Parcelize
@Entity(tableName = "League")
data class League(
    @PrimaryKey @ColumnInfo(name = "idLeague") val idLeague: Long = 0,
    val strSport: String,
    val strLeague: String,
    val follow :Boolean = false
//    val strLeagueAlternate: String
) : Parcelable


data class SportWithLeagueList(
    @Embedded val sprot: Sport,
    @Relation(
        parentColumn = "strSport",
        entityColumn = "strSport",
        entity = League::class
    )
    val leagueslist: List<League>
)






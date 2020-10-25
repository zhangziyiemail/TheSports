package com.example.github.thesports.entity

/**
 *   Created by Lee Zhang on 10/24/20 16:38
 **/

data class EventDetailData(var events: MutableList<EventDetail>)

data class EventDetail(
    val idEvent: String,
    val idSoccerXML: String,
    val idAPIfootball: String,
    val strEvent: String,
    val strEventAlternate: String,
    val idLeague: String,
    val strFilename: String,
    val strLeague: String,
    val strSeason: String,
    val strDescriptionEN: String,
    val strHomeTeam: String,
    val strAwayTeam: String,
    val intHomeScore: String,
    val intRound: String,
    val intAwayScore: String,
    val intSpectators: String,
    val strOfficial: String,
    val strHomeRedCards: String,
    val strHomeYellowCards: String,
    val strHomeLineupGoalkeeper: String,
    val strHomeLineupDefense: String,
    val strHomeLineupMidfield: String,
    val strHomeLineupForward: String,
    val strHomeFormation: String,
    val strHomeLineupSubstitutes: String,
    val strAwayRedCards: String,
    val strAwayYellowCards: String,
    val strAwayGoalDetails: String,
    val strAwayLineupGoalkeeper: String,
    val strHomeGoalDetails: String,
    val strAwayLineupDefense: String,
    val strAwayLineupMidfield: String,
    val strAwayLineupForward: String,
    val strAwayLineupSubstitutes: String,
    val strAwayFormation: String,
    val intHomeShots: String,
    val intAwayShots: String,
    val dateEvent: String,
    val strDate: String,
    val strTime: String,
    val idHomeTeam: String,
    val idAwayTeam: String,
    val strThumb: String
)
package com.example.github.thesports.http

import com.example.github.thesports.entity.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *   Created by Lee Zhang on 10/19/20 15:24
 */

interface ApiService {
    @GET("api/v1/json/1/all_sports.php")
    suspend fun getAllSports(): SportData

    @GET("api/v1/json/1/all_leagues.php")
    suspend fun getAllLeague(): LeagueData

    /**
     * nest 15 leagues
     * https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328
     */
    @GET("api/v1/json/1/eventsnextleague.php")
    suspend fun geteventsnextleague(@Query("id") id: Long): LeagueEventData

    /**
     * spast league
     * https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328
     */
    @GET("api/v1/json/1/eventspastleague.php")
    suspend fun geteventspastleague(@Query("id") id: Long): LeagueEventData



    /**
     * event detail by Id
     * https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=441613
     */
    @GET("/api/v1/json/1/lookupevent.php")
    suspend fun getlookupevent(@Query("id") id: Long): EventDetailData

    /**
     * search event by name
     * https://www.thesportsdb.com/api/v1/json/1/searchevents.php?e=Arsenal_vs_Chelsea
     */
    @GET("/api/v1/json/1/searchevents.php")
    suspend fun getSearchEvent(@Query("e") string: String):LeagueEData


}
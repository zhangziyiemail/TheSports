package com.example.github.thesports.http

import com.example.github.thesports.entity.League
import com.example.github.thesports.entity.LeagueData
import com.example.github.thesports.entity.Sport
import com.example.github.thesports.entity.SportData
import retrofit2.http.GET

/**
 *   Created by Lee Zhang on 10/19/20 15:24
 */

interface ApiService {
    @GET("api/v1/json/1/all_sports.php")
    suspend fun getAllSports(): SportData

    @GET("api/v1/json/1/all_leagues.php")
    suspend fun getAllLeague(): LeagueData


}
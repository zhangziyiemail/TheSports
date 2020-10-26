package com.example.github.thesports.data

import androidx.room.*
import com.example.github.thesports.entity.*

/**
 *   Created by Lee Zhang on 10/19/20 23:51
 **/
@Dao
interface SportDao {

    @Query("select * from Sport")
    suspend fun getSportList(): List<Sport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSportList(sport: List<Sport>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sport: Sport)

    @Transaction
    @Query("SELECT * FROM Sport")
    suspend fun getSportWithLeague(): List<SportWithLeagueList>

}

@Dao
interface LeagueDao {

    @Query("select * from League")
    suspend fun getLeagueList(): List<League>

    @Query("select * from League where follow =:isFollow")
    suspend fun getFollowLeagueList(isFollow: Boolean): List<League>

    @Query("select * from League where strSport = :sport")
    suspend fun getLeagueOfSprotList(sport: String): List<League>

    @Query("update League set follow = :follow where  idLeague = :idLeague")
    suspend fun setfollow(idLeague: Long, follow: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagueList(league: List<League>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: League)


}

@Dao
interface LeagueEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagueList(league: List<LeagueEvent>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: LeagueEvent)

    @Query("select * from LeagueEvent where strLeague =:strleague")
    suspend fun getLeagueList(strleague: String): List<LeagueEvent>

    @Query("select * from LeagueEvent where strLeague =:strleague and strStatus =:mark")
    suspend fun getEndedLeagueList(strleague: String, mark: String): List<LeagueEvent>

    @Query("select strEvent from LeagueEvent where strEvent like '%'|| :event || '%'")
    suspend fun getLikeEvent(event: String): List<String>

    @Transaction
    @Query("SELECT * FROM League where follow =:isFollow")
    suspend fun getFollowLeagueWithEvent(isFollow: Boolean): List<LeagueWithEvent>
}




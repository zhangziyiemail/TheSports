package com.example.github.thesports.data

import androidx.room.*
import com.example.github.thesports.entity.League
import com.example.github.thesports.entity.MyLeagues
import com.example.github.thesports.entity.Sport
import com.example.github.thesports.entity.SportWithLeagueList

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

    @Query("update League set follow = :follow where  idLeague = :idLeague")
    suspend fun setfollow(idLeague: Long, follow : Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagueList(league: List<League>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: League)
}

@Dao
interface MyLeagueDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(myLeagues: MyLeagues)

    @Query("DELETE FROM MyLeagues WHERE idLeague = :idLeague")
    suspend fun deleteByUserId(idLeague: Long)


    @Query("select * from MyLeagues")
    suspend fun getMyLeagues():List<MyLeagues>

    @Query("select *from MyLeagues where strSport IN (:sport)")
    suspend fun getMyLeagyseWithSport(sport: String): List<MyLeagues>
}


/**
 *
select  name ,
hobby = ( stuff((select ',' + hobby from rows_to_row where name = Test.name for xml path('')), 1, 1, '') )
from rows_to_row as Test
group by name
 */



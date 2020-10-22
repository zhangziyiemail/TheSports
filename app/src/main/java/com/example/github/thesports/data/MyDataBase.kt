package com.example.github.thesports.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.github.thesports.MyApplication
import com.example.github.thesports.entity.League
import com.example.github.thesports.entity.MyLeagues
import com.example.github.thesports.entity.Sport

/**
 *   Created by Lee Zhang on 10/19/20 23:36
 **/
@Database(entities = [Sport::class,League::class,MyLeagues::class],version = 1,exportSchema = false)
abstract class MyDataBase : RoomDatabase(){
    abstract fun selectSportDao() : SportDao
    abstract fun leagueDao(): LeagueDao
    abstract fun myLeaguesDao() :MyLeagueDao
}

object MyDatabaseUtils{
    private const val DB_NAME ="my_db"
    private val myDataBase: MyDataBase by lazy {
        Room.databaseBuilder(MyApplication.instance,MyDataBase::class.java,DB_NAME).build()
    }

    val SportDao = myDataBase.selectSportDao()
    val leagueDao = myDataBase.leagueDao()
    val myLeaguesDao =  myDataBase.myLeaguesDao()
}
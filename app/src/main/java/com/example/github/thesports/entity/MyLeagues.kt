package com.example.github.thesports.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 *   Created by Lee Zhang on 10/21/20 00:22
 **/
@Parcelize
@Entity(tableName = "MyLeagues")
data class MyLeagues(
    @PrimaryKey @ColumnInfo(name = "idLeague") val idLeague: Long = 0,
    val strSport: String,
    val strLeague: String
) : Parcelable



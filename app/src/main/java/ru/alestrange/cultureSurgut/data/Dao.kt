package ru.alestrange.cultureSurgut.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import okhttp3.internal.Version

@Dao
interface SurgutCultureVersionDao {
    @Query("SELECT * FROM SurgutCultureVersion WHERE ID=1")
    fun getSurgutCultureVersion(): SurgutCultureVersion?
    @Update
    fun updateVersion(version: SurgutCultureVersion)
    @Insert
    fun insertVersion(version: SurgutCultureVersion)
}

@Dao
interface InterestDao {
    @Query("SELECT * FROM interest")
    fun getAll(): List<Interest>
    @Insert
    fun insertInterest(vararg interest: Interest)
    @Query("delete FROM interest")
    fun deleteAll(): Int
}

@Dao
interface TagDao {
    @Query("SELECT * FROM tag")
    fun getAll(): List<Tag>
    @Insert
    fun insertTag(vararg tag: Tag)
    @Query("delete FROM tag")
    fun deleteAll(): Int
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>
    @Insert
    fun insertHistory(vararg history: History)
    @Query("delete FROM history")
    fun deleteAll(): Int
}
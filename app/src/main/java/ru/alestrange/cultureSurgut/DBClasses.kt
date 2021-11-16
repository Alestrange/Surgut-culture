package ru.alestrange.cultureSurgut

import androidx.room.*
import android.content.Context

@Entity
data class Interest(
    @PrimaryKey val id: Int,
    val systemName: String?,
    val name: String?,
    val imageName: String?,
    val description: String?
)

@Dao
interface InterestDao {
    @Query("SELECT * FROM interest")
    fun getAll(): List<Interest>
    @Insert
    fun insertAll(vararg users: Interest)
}

@Database(entities = [Interest::class], version = 1)
abstract class SurgutCultureDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
}




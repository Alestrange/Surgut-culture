package ru.alestrange.cultureSurgut.data

import androidx.room.*
import android.content.Context
import kotlinx.serialization.Serializable
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.SurgutCultureApplication

@Database(entities = [Interest::class, SurgutCultureVersion::class], version = 3)
abstract class SurgutCultureDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun surgutCultureVersionDao(): SurgutCultureVersionDao
    fun getCurrentVersion():SurgutCultureVersion
    {
        return surgutCultureVersionDao().getSurgutCultureVersion() ?: SurgutCultureVersion(0,0,0,"Version downloading error")
    }
}




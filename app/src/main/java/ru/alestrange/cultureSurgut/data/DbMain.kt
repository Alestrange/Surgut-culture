package ru.alestrange.cultureSurgut.data

import androidx.room.*
import android.content.Context
import kotlinx.serialization.Serializable
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.SurgutCultureApplication

@Database(entities = [Interest::class, SurgutCultureVersion::class, Tag::class, History::class], version = 4)
abstract class SurgutCultureDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun tagDao(): TagDao
    abstract fun historyDao(): HistoryDao
    abstract fun surgutCultureVersionDao(): SurgutCultureVersionDao
    fun getCurrentVersion():SurgutCultureVersion
    {
        return surgutCultureVersionDao().getSurgutCultureVersion() ?: SurgutCultureVersion(0,0,0,"Version downloading error")
    }
}




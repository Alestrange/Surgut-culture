package ru.alestrange.cultureSurgut.data

import androidx.room.*
import android.content.Context
import kotlinx.serialization.Serializable
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.SurgutCultureApplication

@Database(
    entities = [Interest::class, SurgutCultureVersion::class, Tag::class, History::class, CultobjectTag::class, Cultobject::class,
        Illustration::class, CultobjectIllustration::class, HistoryIllustration::class, CultobjectHistory::class, CycleRoute::class, CycleCheckpoint::class],
    version = 14
)
abstract class SurgutCultureDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun tagDao(): TagDao
    abstract fun historyDao(): HistoryDao
    abstract fun cultobjectTagDao(): CultobjectTagDao
    abstract fun cultobjectDao(): CultobjectDao
    abstract fun illustrationDao(): IllustrationDao
    abstract fun cultobjectIllustrationDao(): CultobjectIllustrationDao
    abstract fun historyIllustrationDao(): HistoryIllustrationDao
    abstract fun cultobjectHistoryDao(): CultobjectHistoryDao
    abstract fun cycleRouteDao(): CycleRouteDao
    abstract fun cycleCheckpointDao(): CycleCheckpointDao
    abstract fun surgutCultureVersionDao(): SurgutCultureVersionDao
    fun getCurrentVersion(): SurgutCultureVersion {
        return surgutCultureVersionDao().getSurgutCultureVersion() ?: SurgutCultureVersion(
            0,
            0,
            0,
            "Version downloading error"
        )
    }
}




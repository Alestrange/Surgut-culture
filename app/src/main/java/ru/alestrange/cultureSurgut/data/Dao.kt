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
    @Query("delete FROM SurgutCultureVersion")
    fun deleteAll(): Int
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
    @Query("SELECT * FROM tag where interestId=8")
    fun getSports(): List<Tag>
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>
    @Insert
    fun insertHistory(vararg history: History)
    @Query("delete FROM history")
    fun deleteAll(): Int
    @Query("SELECT * " +
            "FROM history " +
            "where history.periodId=:periodID")
    fun getHistoryByPeriod(periodID:Int): List<History>
    @Query("SELECT * " +
            "FROM history " +
            "where history.id=:historyId")
    fun getHistoryById(historyId: Int): History
}

@Dao
interface CultobjectTagDao {
    @Query("SELECT * FROM CultobjectTag")
    fun getAll(): List<CultobjectTag>
    @Insert
    fun insertCultobjectTag(vararg cultobjectTag: CultobjectTag)
    @Query("delete FROM CultobjectTag")
    fun deleteAll(): Int
}

@Dao
interface CultobjectDao {
    @Query("SELECT * FROM cultobject")
    fun getAll(): List<Cultobject>
    @Query("SELECT distinct cultobject.* " +
            "FROM cultobject, tag, cultobjecttag " +
            "where cultobject.id=cultobjecttag.cultobjectId and cultobjecttag.tagId=tag.id and tag.interestId=:interestID")
    fun getCultobjectByInterest(interestID:Int): List<Cultobject>
    @Query("SELECT * FROM cultobject where cultobject.id=:objectID")
    fun getCultobjectById(objectID:Int): Cultobject
    @Insert
    fun insertCultobject(vararg cultobject: Cultobject)
    @Query("delete FROM cultobject")
    fun deleteAll(): Int
    @Query("SELECT distinct cultobject.* " +
            "FROM cultobject, cultobjecthistory " +
            "where cultobject.id=cultobjecthistory.cultobjectId and cultobjecthistory.historyId=:historyId")
    fun getCultobjectByHistory(historyId: Int): List<Cultobject>
    @Query("SELECT distinct cultobject.* " +
            "FROM cultobject, cultobjecttag " +
            "where cultobject.id=cultobjecttag.cultobjectId and cultobjecttag.tagId=:tagId")
    abstract fun getCultobjectByTag(tagId: Int): List<Cultobject>
}

@Dao
interface IllustrationDao {
    @Query("SELECT * FROM illustration")
    fun getAll(): List<Illustration>
    @Insert
    fun insertInterest(vararg illustration: Illustration)
    @Query("delete FROM illustration")
    fun deleteAll(): Int
    @Query("SELECT distinct illustration.* " +
            "FROM illustration, CultobjectIllustration " +
            "where CultobjectIllustration.illustrationId=Illustration.id and CultobjectIllustration.cultobjectId=:objectID")
    fun getIllustrationByCultobject(objectID:Int): List<Illustration>
    @Query("SELECT distinct illustration.* " +
            "FROM illustration, HistoryIllustration " +
            "where HistoryIllustration.illustrationId=Illustration.id and HistoryIllustration.historyId=:historyId")
    fun getIllustrationByHistory(historyId: Int): List<Illustration>
}


@Dao
interface CultobjectIllustrationDao {
    @Query("SELECT * FROM CultobjectIllustration")
    fun getAll(): List<CultobjectIllustration>
    @Insert
    fun insertCultobjectIllustration(vararg cultobjectIllustration: CultobjectIllustration)
    @Query("delete FROM CultobjectIllustration")
    fun deleteAll(): Int
}

@Dao
interface HistoryIllustrationDao {
    @Query("SELECT * FROM HistoryIllustration")
    fun getAll(): List<HistoryIllustration>
    @Insert
    fun insertHistoryIllustration(vararg historyIllustration: HistoryIllustration)
    @Query("delete FROM HistoryIllustration")
    fun deleteAll(): Int
}

@Dao
interface CultobjectHistoryDao {
    @Query("SELECT * FROM CultobjectHistory")
    fun getAll(): List<CultobjectHistory>
    @Insert
    fun insertCultobjectHistory(vararg cultobjectHistory: CultobjectHistory)
    @Query("delete FROM CultobjectHistory")
    fun deleteAll(): Int
}
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
    fun getCiltobjectByInterest(interestID:Int): List<Cultobject>
    @Query("SELECT * FROM cultobject where cultobject.id=:objectID")
    fun getCultobjectById(objectID:Int): Cultobject
    @Insert
    fun insertCultobject(vararg cultobject: Cultobject)
    @Query("delete FROM cultobject")
    fun deleteAll(): Int
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
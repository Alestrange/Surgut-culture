package ru.alestrange.cultureSurgut.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import ru.alestrange.cultureSurgut.SurgutCultureApplication.Companion.db

interface ImageEntity{
    val image: String?
}

interface CultureEntity{
    val id: Int
    fun deleteAll()
    fun insertRecord()
}

@Entity
@Serializable
data class SurgutCultureVersion (
    @PrimaryKey val id: Int,
    val majorVersion: Int?,
    val minorVersion: Int?,
    val description: String?
)

@Entity
@Serializable
class Interest:ImageEntity, CultureEntity {
    @PrimaryKey override var id: Int = 0
    var name: String? = null
    override var image: String? = null
    var description: String? = null
    fun Interest(id: Int, name: String?, image:String?, description: String?) {
        this.id = id
        this.name = name
        this.image = image
        this.description = description
    }
    override fun deleteAll() {
        db.interestDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.interestDao().insertInterest(this)
    }
}

@Entity
@Serializable
class Illustration:ImageEntity, CultureEntity {
    @PrimaryKey override var id: Int = 0
    var description: String? = null
    override var image: String? = null
    fun Interest(id: Int, description: String?, image:String?) {
        this.id = id
        this.description = description
        this.image = image
    }
    override fun deleteAll()
    {
        db.illustrationDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.illustrationDao().insertInterest(this)
    }
}

@Entity
@Serializable
class Tag:ImageEntity, CultureEntity {
    @PrimaryKey override var id: Int = 0
    var name: String? = null
    override var image: String? = null
    var interestId:Int? = 0
    fun Tag(id: Int, name: String?, image:String?, interestId:Int?) {
        this.id = id
        this.name = name
        this.image = image
        this.interestId=interestId
    }
    override fun deleteAll()
    {
        db.tagDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.tagDao().insertTag(this)
    }
}

@Entity
@Serializable
class History: CultureEntity {
    @PrimaryKey override var id: Int = 0
    var name: String? = null
    var period: String? = null
    var description: String? = null
    fun History(id: Int, name: String?, period:String?, description: String?) {
        this.id = id
        this.name = name
        this.period = period
        this.description = description
    }
    override fun deleteAll()
    {
        db.historyDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.historyDao().insertHistory(this)
    }
}


@Entity
@Serializable
class CultobjectTag: CultureEntity {
    @PrimaryKey override var id: Int = 0
    var cultobjectId:Int? = 0
    var tagId:Int? = 0
    fun CultobjectTag(id: Int, cultobjectId: Int?, tagId:Int?) {
        this.id = id
        this.cultobjectId = cultobjectId
        this.tagId = tagId
    }
    override fun deleteAll()
    {
        db.cultobjectTagDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.cultobjectTagDao().insertCultobjectTag(this)
    }
}

@Entity
@Serializable
class Cultobject: CultureEntity, ImageEntity {
    @PrimaryKey override var id: Int = 0
    var name: String? = null
    override var image: String? = null
    var description: String? = null
    var coordX : Double? = 0.0
    var coordY : Double? = 0.0

    fun Cultobject(id: Int, name: String?, image: String?, description: String?, coordX: Double?, coordY: Double?) {
        this.id = id
        this.name = name
        this.image = image
        this.description = description
        this.coordX = coordX
        this.coordY = coordY
    }
    override fun deleteAll()
    {
        db.cultobjectDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.cultobjectDao().insertCultobject(this)
    }
}

@Entity
@Serializable
class CultobjectIllustration: CultureEntity {
    @PrimaryKey override var id: Int = 0
    var cultobjectId:Int? = 0
    var illustrationId:Int? = 0
    fun CultobjectIllustration(id: Int, cultobjectId: Int?, illustrationId:Int?) {
        this.id = id
        this.cultobjectId = cultobjectId
        this.illustrationId = illustrationId
    }
    override fun deleteAll()
    {
        db.cultobjectIllustrationDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.cultobjectIllustrationDao().insertCultobjectIllustration(this)
    }
}
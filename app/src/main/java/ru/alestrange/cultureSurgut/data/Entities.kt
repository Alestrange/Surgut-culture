package ru.alestrange.cultureSurgut.data

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
    override fun deleteAll()
    {
        db.interestDao().deleteAll()
    }
    override fun insertRecord()
    {
        db.interestDao().insertInterest(this)
    }
}

@Entity
@Serializable
class Tag:ImageEntity, CultureEntity {
    @PrimaryKey override var id: Int = 0
    var name: String? = null
    override var image: String? = null
    fun Tag(id: Int, name: String?, image:String?) {
        this.id = id
        this.name = name
        this.image = image
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
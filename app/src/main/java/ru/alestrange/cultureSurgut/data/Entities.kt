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

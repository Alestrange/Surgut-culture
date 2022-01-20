package ru.alestrange.cultureSurgut.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SurgutCultureVersion(
    @PrimaryKey val id: Int,
    val majorVersion: Int?,
    val minorVersion: Int?,
    val description: String?
)

@Entity
@Serializable
data class Interest(
    @PrimaryKey val id: Int,
    val systemName: String?,
    val name: String?,
    val imageName: String?,
    val description: String?
)
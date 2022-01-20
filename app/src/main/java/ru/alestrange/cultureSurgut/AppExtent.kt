package ru.alestrange.cultureSurgut

import android.app.Application
import androidx.room.Room
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.data.Interest
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.data.SurgutCultureDatabase
import ru.alestrange.cultureSurgut.data.SurgutCultureVersion
import ru.alestrange.cultureSurgut.serverDownload.WebApiCaller
import java.io.BufferedReader

class SurgutCultureApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            SurgutCultureDatabase::class.java, resources.getString(R.string.database_name)
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        version = db.getCurrentVersion()
        val webVersion= WebApiCaller.getSurgutCultureVersion()
        internetConnection = version.id != 0
        if (internetConnection&&((version.majorVersion!=webVersion.majorVersion)||(version.minorVersion!=webVersion.minorVersion)))
        {
            //TODO Update database
            version=webVersion
        }
        val interestStream = resources.openRawResource(R.raw.interest)
        val content = interestStream.bufferedReader().use(BufferedReader::readText)
        val interestsList: List<Interest> = Json.decodeFromString<List<Interest>>(content)
        db.interestDao().deleteAll()
        for (i in interestsList)
            db.interestDao().insertInterest(i)
    }

    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
        lateinit var version: SurgutCultureVersion
            private set
        var internetConnection: Boolean = false
    }
}
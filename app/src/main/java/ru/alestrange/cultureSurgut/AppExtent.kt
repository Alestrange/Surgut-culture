package ru.alestrange.cultureSurgut

import android.app.Application
import androidx.room.Entity
import androidx.room.Room
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.data.*
import ru.alestrange.cultureSurgut.serverDownload.WebApi
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
        if (version.id==0)
            databaseEmpty=true
        val webVersion= WebApiCaller.getSurgutCultureVersion()
        internetConnection = webVersion.id != 0
        if (internetConnection&&((version.majorVersion!=webVersion.majorVersion)||(version.minorVersion!=webVersion.minorVersion)))
        {
            updateDatabase()
            version=webVersion
            if (databaseError==null) {
                if (databaseEmpty)
                    db.surgutCultureVersionDao().insertVersion(version)
                else
                    db.surgutCultureVersionDao().updateVersion(version)
            }
        }
    }

    private fun <T: CultureEntity>updateDatabaseTable(table:T, webApiFunction:suspend ()->List<T>)
    {
        table.deleteAll()
        val res=WebApiCaller.getWebTable(webApiFunction)
        if (res.result.count()==0) {
            databaseError=res.e
            return
        }
        val dataList: List<T> = res.result
        for (i in dataList)
            i.insertRecord()
    }

    private fun updateDatabase()
    {
        updateDatabaseTable(Interest(),WebApi.retrofitService::getInterest)
        updateDatabaseTable(Tag(),WebApi.retrofitService::getTag)
        updateDatabaseTable(History(),WebApi.retrofitService::getHistory)
    }

    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
        lateinit var version: SurgutCultureVersion
            private set
        var internetConnection: Boolean = false
        var databaseEmpty: Boolean = false
        var databaseError:Exception? = null
    }
}
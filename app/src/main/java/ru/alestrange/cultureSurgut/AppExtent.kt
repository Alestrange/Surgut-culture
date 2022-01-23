package ru.alestrange.cultureSurgut

import android.app.Application
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Entity
import androidx.room.Room
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.internal.Version
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.data.*
import ru.alestrange.cultureSurgut.serverDownload.WebApi
import ru.alestrange.cultureSurgut.serverDownload.WebApiCaller
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import android.graphics.Bitmap
import java.io.FileOutputStream


const val imagePath:String="images"
const val imageUrl:String="https://raw.githubusercontent.com/Alestrange/Surgut-culture/master/images/"

class SurgutCultureApplication: Application() {
    private lateinit var imageLoader:ImageLoader
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
        if (true)//if (internetConnection&&((version.majorVersion!=webVersion.majorVersion)||(version.minorVersion!=webVersion.minorVersion)))
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

    private fun insertImage(imageName:String) {
        val path: Path = Paths.get(filesDir + "/" + imagePath+"/"+imageName+".jpg")
        if (!Files.exists(path)) {
            val request = ImageRequest.Builder(applicationContext)
                .data(imageUrl + "\\" + imageName+".jpg")
                .target(
                    onSuccess = { result ->
                            val bm=result.toBitmap()
                            val file = File(imagePath)
                            val outStream = FileOutputStream(file)
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                            outStream.flush()
                            outStream.close()
                    }
                )
                .build()
            imageLoader.enqueue(request)
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
        for (i in dataList) {
            i.insertRecord()
            if (i is ImageEntity) {
                (i as ImageEntity).image?.let {
                    insertImage(it)
                }
            }
        }
    }

    private fun updateDatabase()
    {
        val path: Path = Paths.get(filesDir+"/"+imagePath)
        if (!Files.exists(path)) {
            val f = File(filesDir,imagePath)
            f.mkdirs()
        }
        imageLoader = ImageLoader.Builder(applicationContext)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()
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
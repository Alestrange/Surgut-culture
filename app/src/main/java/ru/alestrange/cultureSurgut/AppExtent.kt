package ru.alestrange.cultureSurgut

import android.app.Application
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Room
import coil.ImageLoader
import coil.request.ImageRequest
import ru.alestrange.cultureSurgut.data.*
import ru.alestrange.cultureSurgut.serverDownload.WebApi
import ru.alestrange.cultureSurgut.serverDownload.WebApiCaller
import java.io.File
import android.graphics.Bitmap
import android.util.Log
import java.io.FileOutputStream


const val imagePath:String="images"
const val imageUrl:String="https://raw.githubusercontent.com/Alestrange/Surgut-culture/master/images/"

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
                db.surgutCultureVersionDao().deleteAll()
                db.surgutCultureVersionDao().insertVersion(version)
            }
        }
    }

    private fun insertImage(imageName:String) {
        if (!File("$filesDir/$imagePath/$imageName.png").exists()) {
            val request = ImageRequest.Builder(applicationContext)
                .data("$imageUrl$imageName.jpg")
                .target(
                    onSuccess = { result ->
                            val bm=result.toBitmap()
                            val file = File("$filesDir/$imagePath/$imageName.png")
                            val outStream = FileOutputStream(file)
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                            outStream.flush()
                            outStream.close()
                            Log.i("mymy", file.exists().toString())
                            Log.i("mymy", "${bm.width.toString()} ${bm.height.toString()}")
                    },
                    onError = { error ->
                        Log.i("mymy", error.toString())
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
        if (!File("$filesDir/$imagePath").exists()) {
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
        updateDatabaseTable(Cultobject(),WebApi.retrofitService::getCultobject)
        updateDatabaseTable(CultobjectTag(),WebApi.retrofitService::getCultobjectTag)
        updateDatabaseTable(Illustration(),WebApi.retrofitService::getIllustration)
        updateDatabaseTable(CultobjectIllustration(),WebApi.retrofitService::getCultobjectIllustration)
    }

    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
        lateinit var version: SurgutCultureVersion
            private set
        lateinit var imageLoader:ImageLoader
        var internetConnection: Boolean = false
        var databaseEmpty: Boolean = false
        var databaseError:Exception? = null
    }
}
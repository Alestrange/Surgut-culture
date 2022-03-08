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
        imageLoader = ImageLoader.Builder(applicationContext)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()
        surgutCultureApplication=this
        version = db.getCurrentVersion()
        if (version.id==0)
            databaseEmpty=true
        val webVersion= WebApiCaller.getSurgutCultureVersion()
        Log.i("mymy", "Current version ${version.minorVersion} Web version: ${webVersion.minorVersion}")
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

    fun insertImage(imageName:String) {
        if (!File("$filesDir/$imagePath/$imageName.png").exists()) {
            imageLoader.memoryCache.clear()
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
                            Log.i("mymy", "Downloaded $imageName  ${bm.width.toString()} ${bm.height.toString()}")
                    },
                    onError = { error ->
                        Log.i("mymy", "Downloading $imageName error: ${error.toString()}")
                    }
                )
                .build()
            imageLoader.enqueue(request)
        }
    }

    private fun <T: CultureEntity>updateDatabaseTable(webApiFunction:suspend ()->List<T>)
    {
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
        Interest().deleteAll()
        Tag().deleteAll()
        History().deleteAll()
        Cultobject().deleteAll()
        CultobjectTag().deleteAll()
        Illustration().deleteAll()
        CultobjectIllustration().deleteAll()
        CultobjectHistory().deleteAll()
        HistoryIllustration().deleteAll()
        CycleRoute().deleteAll()
        CycleCheckpoint().deleteAll()
        if (!File("$filesDir/$imagePath").exists()) {
            val f = File(filesDir,imagePath)
            f.mkdirs()
        }
        updateDatabaseTable<Interest>(WebApi.retrofitService::getInterest)
        updateDatabaseTable<Tag>(WebApi.retrofitService::getTag)
        updateDatabaseTable<History>(WebApi.retrofitService::getHistory)
        updateDatabaseTable<Cultobject>(WebApi.retrofitService::getCultobject)
        updateDatabaseTable<CultobjectTag>(WebApi.retrofitService::getCultobjectTag)
        updateDatabaseTable<Illustration>(WebApi.retrofitService::getIllustration)
        updateDatabaseTable<CultobjectIllustration>(WebApi.retrofitService::getCultobjectIllustration)
        updateDatabaseTable<CultobjectHistory>(WebApi.retrofitService::getCultobjectHistory)
        updateDatabaseTable<HistoryIllustration>(WebApi.retrofitService::getHistoryIllustration)
        updateDatabaseTable<CycleRoute>(WebApi.retrofitService::getCycleRoute)
        updateDatabaseTable<CycleCheckpoint>(WebApi.retrofitService::getCycleCheckpoint)
    }

    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
        lateinit var version: SurgutCultureVersion
            private set
        lateinit var surgutCultureApplication: SurgutCultureApplication
            private set
        lateinit var imageLoader:ImageLoader
        var internetConnection: Boolean = false
        var databaseEmpty: Boolean = false
        var databaseError:Exception? = null
    }
}
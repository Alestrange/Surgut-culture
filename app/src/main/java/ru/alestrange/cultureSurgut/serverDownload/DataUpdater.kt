package ru.alestrange.cultureSurgut.serverDownload

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.executeBlocking
import coil.request.ImageRequest
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.*
import ru.alestrange.cultureSurgut.imagePath
import ru.alestrange.cultureSurgut.imageUrl
import java.io.File
import java.io.FileOutputStream

class DataUpdater {
    companion object{
        val imageListForUpdate:MutableList<String> = mutableListOf()
        lateinit var imageLoader: ImageLoader
        var imageForUpdateCount:Int=0
        var imageForUpdateNum:Int=0

        fun insertImage(imageName:String) {
                imageLoader.memoryCache.clear()
                val request = ImageRequest.Builder(SurgutCultureApplication.surgutCultureApplication.applicationContext)
                    .data("$imageUrl$imageName.jpg")
                    .target(
                        onSuccess = { result ->
                            imageForUpdateNum++
                            val bm=result.toBitmap()
                            val file = File("${SurgutCultureApplication.surgutCultureApplication.filesDir}/$imagePath/$imageName.png")
                            val outStream = FileOutputStream(file)
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                            outStream.flush()
                            outStream.close()
                            Log.i("sclog", "Downloaded $imageName  ${bm.width.toString()} ${bm.height.toString()}")
                        },
                        onError = { error ->
                            imageForUpdateNum++
                            Log.i("sclog", "Downloading $imageName error: ${error.toString()}")
                        }
                    )
                    .build()
                imageLoader.enqueue(request)
        }

        fun <T: CultureEntity>updateDatabaseTable(webApiFunction:suspend ()->List<T>)
        {
            val res=WebApiCaller.getWebTable(webApiFunction)
            if (res.result.count()==0) {
                SurgutCultureApplication.databaseError =res.e
                return
            }
            val dataList: List<T> = res.result
            for (i in dataList) {
                i.insertRecord()
                if (i is ImageEntity) {
                    (i as ImageEntity).image?.let {
                        if (!File("${SurgutCultureApplication.surgutCultureApplication.filesDir}/$imagePath/$it.png").exists()) {
                            if (!imageListForUpdate.contains(it))
                                imageListForUpdate.add(it)
                        }
                    }
                }
            }
        }

        fun updateDatabase()
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
            CultobjectCycleroute().deleteAll()
            if (!File("${SurgutCultureApplication.surgutCultureApplication.filesDir}/$imagePath").exists()) {
                val f = File(SurgutCultureApplication.surgutCultureApplication.filesDir, imagePath)
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
            updateDatabaseTable<CultobjectCycleroute>(WebApi.retrofitService::getCultobjectCycleroute)
            imageForUpdateCount= imageListForUpdate.size
            imageForUpdateNum=0
            imageListForUpdate.forEach {
                insertImage(it)
            }
        }

    }
}
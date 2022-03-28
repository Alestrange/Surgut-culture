package ru.alestrange.cultureSurgut.serverDownload

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
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
        lateinit var imageLoader: ImageLoader

        fun insertImage(imageName:String) {
            if (!File("${SurgutCultureApplication.surgutCultureApplication.filesDir}/$imagePath/$imageName.png").exists()) {
                imageLoader.memoryCache.clear()
                val request = ImageRequest.Builder(SurgutCultureApplication.surgutCultureApplication.applicationContext)
                    .data("$imageUrl$imageName.jpg")
                    .target(
                        onSuccess = { result ->
                            val bm=result.toBitmap()
                            val file = File("${SurgutCultureApplication.surgutCultureApplication.filesDir}/$imagePath/$imageName.png")
                            val outStream = FileOutputStream(file)
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                            outStream.flush()
                            outStream.close()
                            Log.i("sclog", "Downloaded $imageName  ${bm.width.toString()} ${bm.height.toString()}")
                        },
                        onError = { error ->
                            Log.i("sclog", "Downloading $imageName error: ${error.toString()}")
                        }
                    )
                    .build()
                imageLoader.enqueue(request)
            }
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
                        insertImage(it)
                    }
                }
            }
        }

        fun updateDatabase(updateResults: (String, Int) -> Unit)
        {
            updateResults(SurgutCultureApplication.surgutCultureApplication.getString(R.string.update_phase0),0)
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
            updateResults(SurgutCultureApplication.surgutCultureApplication.getString(R.string.update_phase1),2)
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
        }

    }
}
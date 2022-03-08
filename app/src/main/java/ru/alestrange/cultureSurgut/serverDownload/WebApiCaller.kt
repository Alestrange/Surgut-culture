package ru.alestrange.cultureSurgut.serverDownload

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.SurgutCultureVersion

class WebApiResult<T>
(
    var result:List<T> = listOf(),
    var e:java.lang.Exception?=null
)


object WebApiCaller {
    fun getSurgutCultureVersion():SurgutCultureVersion {
        var result= SurgutCultureVersion(0,0,0,"")
        runBlocking {
            var ver = async(context = Dispatchers.IO) {
                try {
                    WebApi.retrofitService.getVersion()
                } catch (e: Exception) {
                    SurgutCultureVersion(0, 0, 0,e.toString())
                }
            }
            result=ver.await()
            Log.i("mymy", "Web (corutine) version ${result.minorVersion}")
        }
        return result
    }

    fun <T>getWebTable(webApiFunction:suspend ()->List<T>): WebApiResult<T> {
        var result= WebApiResult<T>()
        runBlocking {
            var ver = async(context = Dispatchers.IO) {
                try {
                    WebApiResult<T>(webApiFunction(),null)
                } catch (e: Exception) {
                    WebApiResult<T>(listOf<T>(),e)
                }
            }
            result=ver.await()
        }
        return result
    }
}

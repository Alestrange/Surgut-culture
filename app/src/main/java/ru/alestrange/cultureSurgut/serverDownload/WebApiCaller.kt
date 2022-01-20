package ru.alestrange.cultureSurgut.serverDownload

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.alestrange.cultureSurgut.data.SurgutCultureVersion

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
        }
        return result
    }
}

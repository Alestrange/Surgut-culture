package ru.alestrange.cultureSurgut.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater
import ru.alestrange.cultureSurgut.serverDownload.WebApiCaller

class MainViewModel: ViewModel() {

    init {
        Log.d("sclog", "MainViewModel created!")
    }

    fun updateData(){
        viewModelScope.launch(context = Dispatchers.IO, CoroutineStart.DEFAULT)
        {
            SurgutCultureApplication.webVersion = WebApiCaller.getSurgutCultureVersion()
            Log.i(
                "sclog",
                "Current version ${SurgutCultureApplication.version.minorVersion} Web version: ${SurgutCultureApplication.webVersion.minorVersion}"
            )
            SurgutCultureApplication.internetConnection =
                SurgutCultureApplication.webVersion.id != 0
            if (SurgutCultureApplication.internetConnection && ((SurgutCultureApplication.version.majorVersion != SurgutCultureApplication.webVersion.majorVersion) || (SurgutCultureApplication.version.minorVersion != SurgutCultureApplication.webVersion.minorVersion))) {
                Log.i("sclog", "Start updating")
                DataUpdater.updateDatabase()
            } else {
                Log.i("sclog", "No need to updating")
            }
            DataUpdater.dataLoadState=false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("sclog", "MainViewModel destroyed!")
    }
}
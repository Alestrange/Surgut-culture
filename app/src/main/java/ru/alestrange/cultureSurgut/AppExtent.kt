package ru.alestrange.cultureSurgut

import android.app.Activity
import android.app.Application
import android.content.Context
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
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater
import java.io.FileOutputStream


const val imagePath:String="images"
const val imageUrl:String="https://raw.githubusercontent.com/Alestrange/Surgut-culture/master/images/"

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

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
        DataUpdater.imageLoader = ImageLoader.Builder(applicationContext)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()
        surgutCultureApplication=this
        version = db.getCurrentVersion()
        if (version.id==0)
            databaseEmpty=true
        webVersion= WebApiCaller.getSurgutCultureVersion()
        Log.i("sclog", "Current version ${version.minorVersion} Web version: ${webVersion.minorVersion}")
        internetConnection = webVersion.id != 0
    }



    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
        lateinit var version: SurgutCultureVersion
        lateinit var webVersion: SurgutCultureVersion
        lateinit var surgutCultureApplication: SurgutCultureApplication
            private set
        var internetConnection: Boolean = false
        var databaseEmpty: Boolean = false
        var databaseError:Exception? = null
    }
}
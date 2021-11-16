package ru.alestrange.cultureSurgut

import android.app.Application
import androidx.room.Room

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            SurgutCultureDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        //test
        val interests=db.interestDao().getAll()
        val id=interests.count()+1
        val newInterest = Interest(id+1, "test $id", "Тестовое поле $id", "test_image $id", "Тест Описание $id")
        db.interestDao().insertAll(newInterest)
    }

    companion object {
        lateinit var db: SurgutCultureDatabase
            private set
    }
}
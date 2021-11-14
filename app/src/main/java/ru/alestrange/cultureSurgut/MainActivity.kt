package ru.alestrange.cultureSurgut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val db= Room.databaseBuilder(applicationContext, SurgutCultureDatabase::class.java, "surgut-culture-database")
//        db.build()
    }
}
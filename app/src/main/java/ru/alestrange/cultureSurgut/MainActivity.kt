package ru.alestrange.cultureSurgut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.room.*

class MainActivity : AppCompatActivity() {
    private lateinit var db:SurgutCultureDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(
            applicationContext,
            SurgutCultureDatabase::class.java, "database-name"
        ).build()
    }

    fun interestButtonOnClick(view: View) {
        // val myToast = Toast.makeText(this, message, duration);
        val randomIntent = Intent(this, InterestsActivity::class.java)
        startActivity(randomIntent)
    }

    fun nearButtonOnClick(view: View) {
        // val myToast = Toast.makeText(this, message, duration);
        val randomIntent = Intent(this, NearActivity::class.java)
        startActivity(randomIntent)
    }
}
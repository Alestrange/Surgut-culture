package ru.alestrange.cultureSurgut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import androidx.room.*
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.SurgutCultureDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var db:SurgutCultureDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val aboutText : TextView = findViewById(R.id.aboutView)
        aboutText.text= SurgutCultureApplication.version.description?:"No information"
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
package ru.alestrange.cultureSurgut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun button4(view: View) {
        // val myToast = Toast.makeText(this, message, duration);
        val randomIntent = Intent(this, SecondActivity::class.java)
        startActivity(randomIntent)
    }
}
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
import android.view.Menu
import android.view.MenuItem
import ru.alestrange.cultureSurgut.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var db:SurgutCultureDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.aboutView.text= SurgutCultureApplication.version.description?:"No information"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.miInterest -> {
                interestButtonOnClick(binding.interestButton)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
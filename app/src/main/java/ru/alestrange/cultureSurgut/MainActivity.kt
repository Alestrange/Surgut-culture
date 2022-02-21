package ru.alestrange.cultureSurgut

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.alestrange.cultureSurgut.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.aboutView.text=getString(R.string.version_info, SurgutCultureApplication.version.description,SurgutCultureApplication.version.majorVersion,SurgutCultureApplication.version.minorVersion)
        //binding.interestButton.setOnClickListener(this::interestButtonOnClick)
        binding.interestButton.setOnClickListener{_ -> MainMenu.openActivity(this,InterestsActivity())}
        binding.historyButton.setOnClickListener {_ -> MainMenu.openActivity(this,HistoryActivity())}
        binding.activeSurgutButton.setOnClickListener {_ -> MainMenu.openActivity(this,SportActivity())}
        binding.nearButton.setOnClickListener {_ -> MainMenu.openActivity(this,NearActivity())}
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this,item)
    }

}
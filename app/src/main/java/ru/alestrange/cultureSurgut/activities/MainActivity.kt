package ru.alestrange.cultureSurgut.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.alestrange.cultureSurgut.BuildConfig
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.databinding.ActivityMainBinding
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater
import ru.alestrange.cultureSurgut.viewModels.MainViewModel

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val model: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.interestButton.setOnClickListener(this::interestButtonOnClick)
        binding.interestButton.setOnClickListener { _ ->
            MainMenu.openActivity(
                this,
                InterestsActivity()
            )
        }
        binding.historyButton.setOnClickListener { _ ->
            MainMenu.openActivity(
                this,
                HistoryActivity()
            )
        }
        binding.activeSurgutButton.setOnClickListener { _ ->
            MainMenu.openActivity(
                this,
                SportActivity()
            )
        }
        binding.nearButton.setOnClickListener { _ -> MainMenu.openActivity(this, NearActivity()) }
    }

    private fun updateModeOn(){
        DataUpdater.isUpdating=true
        DataUpdater.dataLoadState=true
        binding.loadingLayout.visibility = View.VISIBLE
        binding.buttonLayout.visibility = View.INVISIBLE
        binding.aboutView.text =""
    }

    private fun updateModeOff(){
        if (SurgutCultureApplication.version.id!=0) {
            val versionName: String = BuildConfig.VERSION_NAME
            binding.aboutView.text = getString(
                R.string.version_info, SurgutCultureApplication.version.description,
                versionName,
                SurgutCultureApplication.version.majorVersion,
                SurgutCultureApplication.version.minorVersion
            )
            binding.loadingLayout.visibility = View.INVISIBLE
            binding.buttonLayout.visibility = View.VISIBLE
            DataUpdater.isUpdating=false
        }
        else
        {
            binding.loadingTextView.text=getString(R.string.impossible_first_update)
            binding.aboutView.text = ""
        }
    }

    private fun updateProgress(delayedHandler: Handler) {
        Log.i("sclog", "Update progress ${DataUpdater.dataLoadState} ${DataUpdater.imageForUpdateNum}")
        if ((DataUpdater.dataLoadState) || (DataUpdater.imageForUpdateNum < DataUpdater.imageForUpdateCount)){
            if (!DataUpdater.dataLoadState)
                binding.loadingTextView.text = getString(
                    R.string.update_images_text,
                    DataUpdater.imageForUpdateNum,
                    DataUpdater.imageForUpdateCount
                )
            else
                binding.loadingTextView.text = getString(R.string.update_text)
            delayedHandler.postDelayed({
                updateProgress(delayedHandler)
            }, 100)
        } else {
            updateModeOff()
        }
    }

    override fun onResume() {
        super.onResume()
        if (model.isUpdateChecked)
            return
        model.isUpdateChecked=true
        updateModeOn()
        model.updateData()
        val delayedHandler = Handler(Looper.getMainLooper())
        updateProgress(delayedHandler)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

}
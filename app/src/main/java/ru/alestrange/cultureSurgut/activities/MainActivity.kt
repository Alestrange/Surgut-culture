package ru.alestrange.cultureSurgut.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.databinding.ActivityMainBinding
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.interestButton.setOnClickListener(this::interestButtonOnClick)
        binding.interestButton.setOnClickListener{ _ ->
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

    override fun onResume() {
        super.onResume()
        if (SurgutCultureApplication.internetConnection &&((SurgutCultureApplication.version.majorVersion!=SurgutCultureApplication.webVersion.majorVersion)||(SurgutCultureApplication.version.minorVersion!=SurgutCultureApplication.webVersion.minorVersion)))
        {
            binding.loadingLayout.visibility= View.VISIBLE
            binding.buttonLayout.visibility=View.INVISIBLE
            DataUpdater.updateDatabase(::onUpdateProgress)
            val delayedHandler = Handler(Looper.getMainLooper())
            delayedHandler.postDelayed({
                binding.loadingLayout.visibility= View.INVISIBLE
                binding.buttonLayout.visibility=View.VISIBLE
            }, 2000)
            SurgutCultureApplication.version =SurgutCultureApplication.webVersion
            if (SurgutCultureApplication.databaseError ==null) {
                SurgutCultureApplication.db.surgutCultureVersionDao().deleteAll()
                SurgutCultureApplication.db.surgutCultureVersionDao().insertVersion(
                    SurgutCultureApplication.version
                )
            }
        }
        else
        {
            binding.loadingLayout.visibility= View.INVISIBLE
            binding.buttonLayout.visibility=View.VISIBLE
        }
        binding.aboutView.text=getString(
            R.string.version_info, SurgutCultureApplication.version.description,
            SurgutCultureApplication.version.majorVersion,
            SurgutCultureApplication.version.minorVersion)
    }

    fun onUpdateProgress(message:String,progress:Int)
    {
        binding.loadingIndicator.progress=progress
        binding.loadingTextView.text=message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

}
package ru.alestrange.cultureSurgut.activities

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.databinding.ActivityHistoryDetailBinding

private lateinit var binding: ActivityHistoryDetailBinding

class HistoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val historyId = intent?.extras?.getInt("historyId")
        if (historyId!=null)
        {
            val history = SurgutCultureApplication.db.historyDao().getHistoryById(historyId)
            val illustration = SurgutCultureApplication.db.illustrationDao().getIllustrationByHistory(historyId)
            val cultobjects = SurgutCultureApplication.db.cultobjectDao().getCultobjectByHistory(historyId)
            if (cultobjects.size==0)
                binding.textObjectTitle.visibility=View.INVISIBLE
            binding.textHistoryName.text = history.name
            binding.textHistoryPeriod.text = history.period
            binding.textHistoryDescription.text = history.description
            val objectsView: RecyclerView = binding.objectsView
            objectsView.layoutManager = LinearLayoutManager(this)
            objectsView.adapter =
                ObjectsActivity.ObjectsRecyclerAdapter(cultobjects, baseContext)
            val illustrationView: RecyclerView = binding.illustrationsView
            illustrationView.layoutManager = LinearLayoutManager(this)
            illustrationView.adapter =
                ObjectDetailActivity.IllustrationRecyclerAdapter(illustration, baseContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }



}
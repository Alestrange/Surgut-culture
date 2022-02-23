package ru.alestrange.cultureSurgut

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.data.Illustration
import ru.alestrange.cultureSurgut.databinding.ActivityHistoryDetailBinding
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding

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
        return MainMenu.menuClickHandler(this,item)
    }



}
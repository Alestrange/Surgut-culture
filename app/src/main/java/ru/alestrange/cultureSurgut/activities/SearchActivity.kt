package ru.alestrange.cultureSurgut.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding
import ru.alestrange.cultureSurgut.databinding.ActivitySearchBinding
import ru.alestrange.cultureSurgut.hideKeyboard

private lateinit var binding: ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        val objectsView: RecyclerView = binding.objectsView
        objectsView.layoutManager = LinearLayoutManager(this)
        val cultobjects = listOf<Cultobject>()
        objectsView.adapter = ObjectsActivity.ObjectsRecyclerAdapter(cultobjects, baseContext)
        binding.searchButton.setOnClickListener(::onSearchClickListener)
    }

    fun onSearchClickListener(@Suppress("UNUSED_PARAMETER")view: View) {
        if (binding.searchText.editText?.text.toString()!="") {
            val objects = SurgutCultureApplication.db.cultobjectDao()
                .getCultobjectByText(binding.searchText.editText?.text.toString())
            if (objects.isNotEmpty()) {
                binding.messageText.visibility = View.INVISIBLE
                binding.objectsView.visibility = View.VISIBLE
                (binding.objectsView.adapter as ObjectsActivity.ObjectsRecyclerAdapter).update(
                    objects
                )
            } else {
                binding.objectsView.visibility = View.INVISIBLE
                binding.messageText.visibility = View.VISIBLE
                binding.messageText.text = getString(R.string.search_not_found)
            }
        }
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }
}
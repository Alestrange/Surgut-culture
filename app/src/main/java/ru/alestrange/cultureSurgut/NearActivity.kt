package ru.alestrange.cultureSurgut

import android.os.Bundle
import android.text.Editable
import android.widget.MultiAutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding

class NearActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_near)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val interests= MyApp.db.interestDao().getAll()
        val json = Json.encodeToJsonElement(interests)
        val jsonText : MultiAutoCompleteTextView = findViewById(R.id.testJsonText)
        jsonText.text  = json.toString() as Editable
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_near)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
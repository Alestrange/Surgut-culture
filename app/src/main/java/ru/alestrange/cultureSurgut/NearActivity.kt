package ru.alestrange.cultureSurgut

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
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
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding
import java.io.BufferedReader

class NearActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near)
        val interests= SurgutCultureApplication.db.interestDao().getAll()
        val json = Json.encodeToJsonElement(interests)
        val jsonText : EditText = findViewById(R.id.editTextJ)
        jsonText.setText(json.toString())

        //
        //jsonText.setText(interestsList.count())
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_near)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
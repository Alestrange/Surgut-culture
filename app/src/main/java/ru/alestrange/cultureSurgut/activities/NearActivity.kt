package ru.alestrange.cultureSurgut.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import ru.alestrange.cultureSurgut.Haversine
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding


private lateinit var binding: ActivityNearBinding
var distance: Int = 1000
var deviceLocation: Location? = null
private lateinit var allCultobjects: List<Cultobject>

private lateinit var fusedLocationClient: FusedLocationProviderClient

class NearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        val objectsView: RecyclerView = binding.objectsView
        objectsView.layoutManager = LinearLayoutManager(this)
        allCultobjects = SurgutCultureApplication.db.cultobjectDao().getAll()
        val cultobjects = listOf<Cultobject>()
        objectsView.adapter = ObjectsActivity.ObjectsRecyclerAdapter(cultobjects, baseContext)
        val listener = SeekBarListener()
        binding.distBar.setProgress(distance / 100, false)
        listener.changeDistance(baseContext, distance / 100)
        binding.distBar.setOnSeekBarChangeListener(listener)
        markButtonDisable(binding.searchButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.searchButton.setOnClickListener(::onSearchClickListener)
        binding.updateButton.setOnClickListener(::onUpdateClickListener)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
               ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
                return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(::onSuccessListener)
            .addOnFailureListener {
                Log.i("mymy", it.toString())
            }
    }

    fun onSuccessListener(location: Location?) {
        deviceLocation = location
        location?.let {
            binding.messageText.text = resources.getString(
                R.string.search_location_enabled,
                it.latitude,
                it.longitude
            )
            markButtonEnable(binding.searchButton)
        }
            ?: run {
                binding.messageText.text =
                    resources.getString(R.string.impossible_seek_location_not_yet)
                markButtonDisable(binding.searchButton)
            }
    }


    fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.setTextColor(ContextCompat.getColor(button.context, R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.grayish))
    }

    fun markButtonEnable(button: Button) {
        button.isEnabled = true
        button.setTextColor(ContextCompat.getColor(button.context, R.color.black))
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.green_surgut_dark))
    }

    fun onUpdateClickListener(@Suppress("UNUSED_PARAMETER")view: View) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
                return
            }
        val locationRequest = LocationRequest.create()
        fusedLocationClient.requestLocationUpdates(
            locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                     onSuccessListener(locationResult.lastLocation)
                     binding.messageText.text=resources.getString(
                         R.string.search_location_updated,
                         locationResult.lastLocation?.latitude,
                         locationResult.lastLocation?.longitude
                     )
                     fusedLocationClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    fun onSearchClickListener(@Suppress("UNUSED_PARAMETER")view: View) {
        if (deviceLocation != null) {
            val center = Haversine(deviceLocation!!.latitude, deviceLocation!!.longitude)
            val nearCultobject = allCultobjects
                .filter {
                    if ((it.coordX == null) || (it.coordY == null))
                        false
                    else {
                        val limit: Double = distance.toDouble() / 1000.0
                        val destination = Haversine(it.coordX!!, it.coordY!!)
                        val d = center.haversine(destination)
                        d <= limit
                    }
                }
            if (nearCultobject.isNotEmpty()) {
                binding.objectsView.visibility = View.VISIBLE
                binding.messageLayout.visibility = View.INVISIBLE
                (binding.objectsView.adapter as ObjectsActivity.ObjectsRecyclerAdapter).update(
                    nearCultobject
                )
            } else {
                binding.objectsView.visibility = View.INVISIBLE
                binding.messageLayout.visibility = View.VISIBLE
                binding.messageText.text = getString(R.string.search_not_found)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener(::onSuccessListener)
                    }
                } else {
                    Toast.makeText(
                        this,
                        resources.getText(R.string.impossible_seek_location),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.messageText.text = resources.getText(R.string.impossible_seek_location)
                }
                return
            }
        }
    }

    class SeekBarListener : SeekBar.OnSeekBarChangeListener {

        fun changeDistance(context: Context, newValue: Int) {
            distance = newValue * 100
            if (distance < 1000)
                binding.distValueText.text = context.getString(R.string.distance_meters, distance)
            else
                binding.distValueText.text =
                    context.getString(R.string.distance_kms, distance / 1000)
        }

        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
            changeDistance(seekBar.context, i)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }
}
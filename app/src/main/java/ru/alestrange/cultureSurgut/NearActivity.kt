package ru.alestrange.cultureSurgut

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import ru.alestrange.cultureSurgut.data.Cultobject

private lateinit var binding: ActivityNearBinding
var distance: Int = 1000
var deviceLocation: Location? = null
private lateinit var allCultobjects : List<Cultobject>

private lateinit var fusedLocationClient: FusedLocationProviderClient

class NearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearBinding.inflate(layoutInflater)
        val view:View = binding.root
        setContentView(view)
        val objectsView: RecyclerView = binding.objectsView
        objectsView.layoutManager = LinearLayoutManager(this)
        allCultobjects=SurgutCultureApplication.db.cultobjectDao().getAll()
        val cultobjects = listOf<Cultobject>()
        objectsView.adapter = ObjectsActivity.ObjectsRecyclerAdapter(cultobjects, baseContext)
        val listener = SeekBarListener()
        binding.distBar.setProgress(distance / 100, false)
        listener.changeDistance(baseContext, distance / 100)
        binding.distBar.setOnSeekBarChangeListener(listener)
        binding.searchButton.text = resources.getText(R.string.seek_location_in_process)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                deviceLocation = location
                binding.searchButton.text =
                    //"${location?.longitude} ${location?.latitude}"
                     resources.getText(R.string.search_button)
            }
            .addOnFailureListener {
                Log.i("mymy", it.toString())
            }
        binding.searchButton.setOnClickListener(::onSearchClickListener)
    }

    fun onSearchClickListener(view: View){
        if (deviceLocation!=null) {
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
            (binding.objectsView.adapter as ObjectsActivity.ObjectsRecyclerAdapter).update(
                nearCultobject
            )
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
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                deviceLocation = location
                                binding.searchButton.text =
                                    //"${location?.longitude} ${location?.latitude}"
                                    resources.getText(R.string.search_button)
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        resources.getText(R.string.impossible_seek_location),
                        Toast.LENGTH_SHORT
                    ).show()
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
}
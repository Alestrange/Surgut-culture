package ru.alestrange.cultureSurgut

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.databinding.ActivityNearBinding
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding

private lateinit var binding: ActivityNearBinding
var distance: Int = 1000


class NearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val objectsView: RecyclerView = binding.objectsView
        objectsView.layoutManager = LinearLayoutManager(this)
        val cultobjects = SurgutCultureApplication.db.cultobjectDao().getAll()
        objectsView.adapter = ObjectsActivity.ObjectsRecyclerAdapter(cultobjects, baseContext)
        val listener = SeekBarListener()
        binding.distBar.setProgress(distance / 100, false)
        listener.changeDistance(baseContext ,distance / 100)
        binding.distBar.setOnSeekBarChangeListener(listener)
    }


    class SeekBarListener : SeekBar.OnSeekBarChangeListener {

        fun changeDistance(context:Context, newValue: Int) {
            distance = newValue * 100
            if (distance < 1000)
                binding.distValueText.text = context.getString(R.string.distance_meters, distance)
            else
                binding.distValueText.text = context.getString(R.string.distance_kms, distance / 1000)
        }

        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
            changeDistance(seekBar.context, i)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
}
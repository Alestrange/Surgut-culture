package ru.alestrange.cultureSurgut

import android.R.attr.*
import android.graphics.Matrix
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.alestrange.cultureSurgut.databinding.ActivityHistoryBinding

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
private lateinit var binding: ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private var previousX: Float = 0.0F
    private var previousY: Float = 0.0F
    private var angle: Float = 0.0F
    private  var historyPeriod : Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        angle = 30.0F
        setAngle()
    }

    private fun setAngle()
    {
        binding.imageView.rotation = angle
        var approxAngle=angle%360
        if (approxAngle<0)
            approxAngle+=360
        when (approxAngle)
        {
            in 0F..60.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_6)
                historyPeriod = 6
            }
            in 60.1F..120.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_5)
                historyPeriod = 5
            }
            in 120.1F..180.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_4)
                historyPeriod = 4
            }
            in 180.1F..240.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_3)
                historyPeriod = 3
            }
            in 240.1F..300.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_2)
                historyPeriod = 2
            }
            in 300.1F..360.0F -> {
                binding.periodDescriptionText.text = getString(R.string.history_period_1)
                historyPeriod = 1
            }
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = x - previousX
                var dy: Float = y - previousY
                if (y > binding.historyLayout.height / 2)
                    dx *= -1
                if (x < binding.historyLayout.width / 2)
                    dy *= -1
                angle += (dx + dy) * TOUCH_SCALE_FACTOR
                setAngle()
            }
        }
        previousX = x
        previousY = y
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }
}
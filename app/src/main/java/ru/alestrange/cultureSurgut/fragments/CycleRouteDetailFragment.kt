package ru.alestrange.cultureSurgut.fragments

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.activities.CycleDetailActivity
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteDetailBinding
import ru.alestrange.cultureSurgut.imagePath
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater
import java.io.File

class CycleRouteDetailFragment : Fragment() {
    private lateinit var binding: FragmentCycleRouteDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCycleRouteDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.textCycleRouteComplex.text=getString(R.string.cycleroute_complex, CycleDetailActivity.cycleRoute?.complex)
        binding.textCycleRouteRoad.text=getString(R.string.cycleroute_road, CycleDetailActivity.cycleRoute?.road)
        binding.textCycleRouteDistance.text=getString(
            R.string.cycleroute_distance,
            CycleDetailActivity.cycleRoute?.distance)
        binding.textHistoryDescription.text= CycleDetailActivity.cycleRoute?.description
        if (!File("${context?.filesDir}/$imagePath/${CycleDetailActivity.cycleRoute?.image}.jpg").exists())
            CycleDetailActivity.cycleRoute?.image?.let {
                DataUpdater.insertImage(it)
            }
        val bm =
            BitmapFactory.decodeFile("${context?.filesDir}/$imagePath/${CycleDetailActivity.cycleRoute?.image}.jpg")
        Log.i(
            "sclog",
            "result img ${CycleDetailActivity.cycleRoute?.image}.jpg ${bm?.width} ${bm?.height}"
        )
        val d: Drawable = BitmapDrawable(resources, bm)
        binding.imageCycleRoute.setImageDrawable(d)
        binding.imageCycleRoute.setOnClickListener {
            CycleDetailActivity.binding.viewPager.currentItem = 3
        }
        return view
    }
}
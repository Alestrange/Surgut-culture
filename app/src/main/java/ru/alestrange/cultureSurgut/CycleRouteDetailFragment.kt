package ru.alestrange.cultureSurgut

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alestrange.cultureSurgut.databinding.ActivityHistoryBinding.inflate
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteDetailBinding
import ru.alestrange.cultureSurgut.databinding.TagviewItemBinding.inflate
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
        binding.textCycleRouteRoad.text=getString(R.string.cycleroute_road,CycleDetailActivity.cycleRoute?.road)
        binding.textCycleRouteDistance.text=getString(R.string.cycleroute_distance,CycleDetailActivity.cycleRoute?.distance)
        binding.textHistoryDescription.text=CycleDetailActivity.cycleRoute?.description
        if (!File("${context?.filesDir}/$imagePath/${CycleDetailActivity.cycleRoute?.image}.png").exists())
            CycleDetailActivity.cycleRoute?.image?.let {
                SurgutCultureApplication.surgutCultureApplication.insertImage(it)
            }
        val bm =
            BitmapFactory.decodeFile("${context?.filesDir}/$imagePath/${CycleDetailActivity.cycleRoute?.image}.png")
        Log.i(
            "mymy",
            "result img ${CycleDetailActivity.cycleRoute?.image}.png ${bm?.width} ${bm?.height}"
        )
        val d: Drawable = BitmapDrawable(resources, bm)
        binding.imageCycleRoute.setImageDrawable(d)
        return view
    }
}
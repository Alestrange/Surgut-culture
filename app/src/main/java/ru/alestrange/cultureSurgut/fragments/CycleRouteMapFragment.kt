package ru.alestrange.cultureSurgut.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alestrange.cultureSurgut.activities.CycleDetailActivity
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteMapBinding
import ru.alestrange.cultureSurgut.imagePath


class CycleRouteMapFragment : Fragment() {

    private lateinit var binding: FragmentCycleRouteMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCycleRouteMapBinding.inflate(inflater, container, false)
        val view = binding.root
        var bm =
            BitmapFactory.decodeFile("${context?.filesDir}/$imagePath/${CycleDetailActivity.cycleRoute?.image}.jpg")
        if (bm.width>bm.height) {
            val matrix = Matrix()
            matrix.postRotate(90F)
             bm = Bitmap.createBitmap(
                bm,
                0,
                0,
                bm.width,
                bm.height,
                matrix,
                true
            )
        }
        Log.i(
            "sclog",
            "result img ${CycleDetailActivity.cycleRoute?.image}.jpg ${bm?.width} ${bm?.height}"
        )
        val d: Drawable = BitmapDrawable(resources, bm)
        binding.imageCycleRoute.setImageDrawable(d)
        binding.imageCycleRoute.setOnClickListener {
            CycleDetailActivity.binding.viewPager.currentItem = 1
        }
        return view
    }


}
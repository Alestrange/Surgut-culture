package ru.alestrange.cultureSurgut.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.activities.CycleDetailActivity
import ru.alestrange.cultureSurgut.activities.ObjectsActivity
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteCheckpointBinding
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteCultobjectBinding

class CycleRouteCultobjectFragment : Fragment() {
    private lateinit var binding: FragmentCycleRouteCultobjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCycleRouteCultobjectBinding.inflate(inflater, container, false)
        val view = binding.root
        val cultobjectsView: RecyclerView = binding.cultobjectsView
        cultobjectsView.layoutManager = LinearLayoutManager(context)
        CycleDetailActivity.cultobjects?.let {
            cultobjectsView.adapter =
                ObjectsActivity.ObjectsRecyclerAdapter(
                    it,
                    requireContext()
                )
        }

        return view
    }
}
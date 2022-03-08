package ru.alestrange.cultureSurgut

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.data.CycleCheckpoint
import ru.alestrange.cultureSurgut.data.Illustration
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteCheckpointBinding
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteDetailBinding

class CycleRouteCheckpointFragment : Fragment() {
    private lateinit var binding: FragmentCycleRouteCheckpointBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCycleRouteCheckpointBinding.inflate(inflater, container, false)
        val view = binding.root
        val checkpointView: RecyclerView = binding.checkpointView
        checkpointView.layoutManager = LinearLayoutManager(context)
        CycleDetailActivity.checkpoints?.let {
            checkpointView.adapter =
                CycleRouteCheckpointFragment.CheckpointRecyclerAdapter(
                    it,
                    requireContext()
                )
        }
        return view
    }

    class CheckpointRecyclerAdapter(private val illustrations: List<CycleCheckpoint>, val context: Context):
        RecyclerView.Adapter<CycleRouteCheckpointFragment.CheckpointRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var checkpointTextView: TextView? = null
            var checkpointImageView: ImageView? = null
            var checkpointButton: Button? = null
            init {
                checkpointTextView = itemView.findViewById(R.id.textName)
                checkpointImageView = itemView.findViewById(R.id.imageObject)
                checkpointButton = itemView.findViewById(R.id.mapButton)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.checkpointview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            CycleDetailActivity.checkpoints?.let {checkpoint ->
                holder.checkpointTextView?.text =
                    checkpoint[position].description
                val bm =
                    BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${checkpoint[position].image}.png")
                Log.i("mymy", "result img ${bm?.width} ${bm?.height}")
                val d: Drawable = BitmapDrawable(context.resources, bm)
                holder.checkpointImageView?.setImageDrawable(d)
                holder.checkpointButton?.setOnClickListener {
                    val geoUriString = "geo:${checkpoint[position].coordX},${checkpoint[position].coordY}?z=15"
                    val geoUri: Uri = Uri.parse(geoUriString)
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    context.startActivity(mapIntent)
                }
            }
        }

        override fun getItemCount(): Int {
            return CycleDetailActivity.checkpoints?.count()?:0
        }

    }
}
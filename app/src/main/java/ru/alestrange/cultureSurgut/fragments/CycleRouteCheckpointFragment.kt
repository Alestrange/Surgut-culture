package ru.alestrange.cultureSurgut.fragments

import android.content.ActivityNotFoundException
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.activities.CycleDetailActivity
import ru.alestrange.cultureSurgut.data.CycleCheckpoint
import ru.alestrange.cultureSurgut.databinding.FragmentCycleRouteCheckpointBinding
import ru.alestrange.cultureSurgut.imagePath

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
                CheckpointRecyclerAdapter(
                    it,
                    requireContext()
                )
        }
        return view
    }

    class CheckpointRecyclerAdapter(private val illustrations: List<CycleCheckpoint>, val context: Context):
        RecyclerView.Adapter<CheckpointRecyclerAdapter.MyViewHolder>()
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
            CycleDetailActivity.checkpoints?.let { checkpoint ->
                holder.checkpointTextView?.text =
                    checkpoint[position].description
                val bm =
                    BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${checkpoint[position].image}.jpg")
                Log.i("mymy", "result img ${bm?.width} ${bm?.height}")
                bm?.let{
                    val d: Drawable = BitmapDrawable(context.resources, it)
                    holder.checkpointImageView?.setImageDrawable(d)
                } ?: run{
                    holder.checkpointImageView?.visibility=View.GONE
                }
                holder.checkpointButton?.contentDescription= context.getString(R.string.map_button_description, checkpoint[position].description)
                holder.checkpointButton?.setOnClickListener {
                    //val geoUriString = "geo:${checkpoint[position].coordX},${checkpoint[position].coordY}?z=12"
                    val geoUriString = "geo:0,0?q=${checkpoint[position].coordX},${checkpoint[position].coordY}"
                    val geoUri: Uri = Uri.parse(geoUriString)
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    try{
                        context.startActivity(mapIntent)
                    }
                    catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            this.context,
                            this.context.getString(R.string.impossible_find_map_application),
                            Toast.LENGTH_SHORT
                        ).show()
                    }                }
            }
        }

        override fun getItemCount(): Int {
            return CycleDetailActivity.checkpoints?.count()?:0
        }

    }
}
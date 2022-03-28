package ru.alestrange.cultureSurgut.activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.CycleRoute
import ru.alestrange.cultureSurgut.databinding.ActivityCycleRouteBinding
import ru.alestrange.cultureSurgut.imagePath
import ru.alestrange.cultureSurgut.serverDownload.DataUpdater
import java.io.File

private lateinit var binding: ActivityCycleRouteBinding
private lateinit var activityCycleRoutes:List<CycleRoute>

class CycleRouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCycleRouteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.cyclerouteView.layoutManager = LinearLayoutManager(this)
        activityCycleRoutes = SurgutCultureApplication.db.cycleRouteDao().getAll()
        binding.cyclerouteView.adapter = CycleRouteRecyclerAdapter(
            activityCycleRoutes, baseContext
        )
        binding.difficultyButton.setOnClickListener{
            activityCycleRoutes = SurgutCultureApplication.db.cycleRouteDao().getAllSortedByDifficulty()
            (binding.cyclerouteView.adapter as CycleRouteRecyclerAdapter).update()
        }
        binding.distanceButton.setOnClickListener{
            activityCycleRoutes = SurgutCultureApplication.db.cycleRouteDao().getAllSortedByDistance()
            (binding.cyclerouteView.adapter as CycleRouteRecyclerAdapter).update()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    class CycleRouteRecyclerAdapter(
        private var cycleRoutes: List<CycleRoute>,
        val context: Context
    ) :
        RecyclerView.Adapter<CycleRouteRecyclerAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameTextView: TextView? = null
            var complexTextView: TextView? = null
            var roadTextView: TextView? = null
            var dintanceTextView: TextView? = null
            var imageView: ImageView? = null

            init {
                nameTextView = itemView.findViewById(R.id.textCycleRouteName)
                complexTextView = itemView.findViewById(R.id.textCycleRouteComplex)
                roadTextView = itemView.findViewById(R.id.textCycleRouteRoad)
                dintanceTextView = itemView.findViewById(R.id.textCycleRouteDistance)
                imageView = itemView.findViewById(R.id.imageCycleRoute)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cycleroute_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.nameTextView?.text = cycleRoutes[position].name
            holder.complexTextView?.text =
                context.getString(R.string.cycleroute_complex, cycleRoutes[position].complex)
            holder.roadTextView?.text =
                context.getString(R.string.cycleroute_road, cycleRoutes[position].road)
            holder.dintanceTextView?.text =
                context.getString(R.string.cycleroute_distance, cycleRoutes[position].distance)

            if (!File("${context.filesDir}/$imagePath/${cycleRoutes[position].image}.png").exists())
                cycleRoutes[position].image?.let {
                    DataUpdater.insertImage(it)
                }
            val bm =
                BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${cycleRoutes[position].image}.png")
            Log.i(
                "mymy",
                "result img ${cycleRoutes[position].image}.png ${bm?.width} ${bm?.height}"
            )
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.imageView?.setImageDrawable(d)
            holder.imageView?.tag = cycleRoutes[position].id
            holder.imageView?.setOnClickListener {
                val context = it.context
                val intent = Intent(context, CycleDetailActivity::class.java)
                intent.putExtra("cycleRouteId", it.tag as Int)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return cycleRoutes.count()
        }

        fun update(){
            this.cycleRoutes = activityCycleRoutes
            notifyDataSetChanged()
        }
    }

}
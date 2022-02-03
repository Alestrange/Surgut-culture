package ru.alestrange.cultureSurgut

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.data.Illustration
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding

private lateinit var binding: ActivityObjectDetailBinding

class ObjectDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val objectId = intent?.extras?.getInt("objectId")
        objectId?.let {
            val cultobject = SurgutCultureApplication.db.cultobjectDao().getCultobjectById(objectId)
            binding.textObjectName.text = cultobject.name
            binding.textObjectDescription.text = cultobject.description
            val bm = BitmapFactory.decodeFile("${applicationContext.filesDir}/$imagePath/${cultobject.image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(applicationContext.resources, bm)
            binding.imageObject?.setImageDrawable(d)
            val objectsView: RecyclerView = findViewById(R.id.illustrationsView)
            objectsView.layoutManager = LinearLayoutManager(this)
            val cultobjects = SurgutCultureApplication.db.illustrationDao().getIllustrationByCultobject(objectId)
            objectsView.adapter = IllustrationRecyclerAdapter(cultobjects, baseContext)
        }
    }

    class IllustrationRecyclerAdapter(private val illustrations: List<Illustration>, val context: Context):
        RecyclerView.Adapter<IllustrationRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var objectTextView: TextView? = null
            var objectImageView: ImageView? = null

            init {
                objectTextView = itemView.findViewById(R.id.textIllustrationName)
                objectImageView = itemView.findViewById(R.id.imageObject)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.illustrationview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.objectTextView?.text = illustrations[position].description
            val bm = BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${illustrations[position].image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.objectImageView?.setImageDrawable(d)
        }

        override fun getItemCount(): Int {
            return illustrations.count()
        }

    }


}
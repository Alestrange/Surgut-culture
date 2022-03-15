package ru.alestrange.cultureSurgut.activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.data.Illustration
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding
import ru.alestrange.cultureSurgut.imagePath

private lateinit var binding: ActivityObjectDetailBinding

private var cultobject:Cultobject?=null

class ObjectDetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val objectId = intent?.extras?.getInt("objectId")
        if (objectId!=null)
        {
            cultobject = SurgutCultureApplication.db.cultobjectDao().getCultobjectById(objectId)
            binding.textObjectName.text = cultobject?.name
            binding.textObjectDescription.text = cultobject?.description
            val bm = BitmapFactory.decodeFile("${applicationContext.filesDir}/$imagePath/${cultobject?.image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(applicationContext.resources, bm)
            binding.imageObject.setImageDrawable(d)
            val objectsView: RecyclerView = findViewById(R.id.illustrationsView)
            objectsView.layoutManager = LinearLayoutManager(this)
            val cultobjects = SurgutCultureApplication.db.illustrationDao().getIllustrationByCultobject(objectId)
            objectsView.adapter = IllustrationRecyclerAdapter(cultobjects, baseContext)
            binding.mapButton.setOnClickListener{ _ -> onMapClick()}
        }
    }

    private fun onMapClick()
    {
        val geoUriString = "geo:${cultobject?.coordX},${cultobject?.coordY}?z=15"
        val geoUri: Uri = Uri.parse(geoUriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        startActivity(mapIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
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
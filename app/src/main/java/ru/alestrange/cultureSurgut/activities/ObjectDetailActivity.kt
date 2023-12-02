package ru.alestrange.cultureSurgut.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.data.Illustration
import ru.alestrange.cultureSurgut.data.Link
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding
import ru.alestrange.cultureSurgut.imagePath

private lateinit var binding: ActivityObjectDetailBinding

private var cultobject: Cultobject? = null

class ObjectDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val objectId = intent?.extras?.getInt("objectId")
        if (objectId != null) {
            cultobject = SurgutCultureApplication.db.cultobjectDao().getCultobjectById(objectId)
            binding.textObjectName.text = cultobject?.name
            binding.textObjectDescription.text = cultobject?.description
            val bm =
                BitmapFactory.decodeFile("${applicationContext.filesDir}/$imagePath/${cultobject?.image}.jpg")
            Log.i("sclog", "result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(applicationContext.resources, bm)
            binding.imageObject.setImageDrawable(d)
            val objectsView: RecyclerView = binding.illustrationsView
            objectsView.layoutManager = LinearLayoutManager(this)
            val cultobjects =
                SurgutCultureApplication.db.illustrationDao().getIllustrationByCultobject(objectId)
            objectsView.adapter = IllustrationRecyclerAdapter(cultobjects, baseContext)
            val linksView: RecyclerView = binding.linkView
            linksView.layoutManager = LinearLayoutManager(this)
            val links = SurgutCultureApplication.db.linkDao().getLinkByCultobject(objectId)
            linksView.adapter = LinkRecyclerAdapter(links, baseContext)
            binding.mapButton.setOnClickListener { _ -> onMapClick() }
        }
    }

    private fun onMapClick() {
        val geoUriString = "geo:${cultobject?.coordX},${cultobject?.coordY}?z=15"
        val geoUri: Uri = Uri.parse(geoUriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.impossible_find_map_application),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    class IllustrationRecyclerAdapter(
        private val illustrations: List<Illustration>,
        val context: Context
    ) :
        RecyclerView.Adapter<IllustrationRecyclerAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            val bm =
                BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${illustrations[position].image}.jpg")
            Log.i("sclog", "result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.objectImageView?.setImageDrawable(d)
        }

        override fun getItemCount(): Int {
            return illustrations.count()
        }
    }

    class LinkRecyclerAdapter(private val links: List<Link>, val context: Context) :
        RecyclerView.Adapter<LinkRecyclerAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var linkButtom: Button? = null
            init {
                linkButtom = itemView.findViewById(R.id.linkButton)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.linkview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.linkButtom?.tag = links[position].id
            holder.linkButtom?.text = links[position].description
            holder.linkButtom?.contentDescription = context.getString(
                R.string.button_detail_description,
                links[position].description
            )
            holder.linkButtom?.setOnClickListener {
                if ((links[position].web!=null)&&(links[position].web!="")) {
                    val context = it.context
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(links[position].web)
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.impossible_find_browser_application),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return links.count()
        }
    }

}
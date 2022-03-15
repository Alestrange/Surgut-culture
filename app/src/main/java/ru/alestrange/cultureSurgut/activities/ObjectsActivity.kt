package ru.alestrange.cultureSurgut.activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import ru.alestrange.cultureSurgut.imagePath


class ObjectsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objects)
        val objectsView: RecyclerView = findViewById(R.id.objectsView)
        objectsView.layoutManager = LinearLayoutManager(this)
        val interestId = intent?.extras?.getInt("interestId")
        val tagId = intent?.extras?.getInt("tagId")
        interestId?.let {
            if (interestId>0) {
                val cultobjects =
                    SurgutCultureApplication.db.cultobjectDao().getCultobjectByInterest(interestId)
                objectsView.adapter = ObjectsRecyclerAdapter(cultobjects, baseContext)
            }
        }
        tagId?.let {
            if (tagId>0) {
                val cultobjects =
                    SurgutCultureApplication.db.cultobjectDao().getCultobjectByTag(tagId)
                objectsView.adapter = ObjectsRecyclerAdapter(cultobjects, baseContext)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    class ObjectsRecyclerAdapter(private var cultobjects: List<Cultobject>, val context: Context):
        RecyclerView.Adapter<ObjectsRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var objectTextView: TextView? = null
            var objectImageView: ImageView? = null

            init {
                objectTextView = itemView.findViewById(R.id.textObjectName)
                objectImageView = itemView.findViewById(R.id.imageObject)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.objectsview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.objectTextView?.text = cultobjects[position].name
            val bm = BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${cultobjects[position].image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.objectImageView?.setImageDrawable(d)
            holder.objectImageView?.tag=cultobjects[position].id
            holder.objectImageView?.setOnClickListener {
                val context = it.context
                val intent = Intent(context, ObjectDetailActivity::class.java)
                intent.putExtra("objectId", it.tag as Int)
                context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return cultobjects.count()
        }

        fun update(cultobjects: List<Cultobject>){
            this.cultobjects=cultobjects
            notifyDataSetChanged()
        }

    }
}
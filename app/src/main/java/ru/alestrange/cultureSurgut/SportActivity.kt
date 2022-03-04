package ru.alestrange.cultureSurgut

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.data.Interest
import ru.alestrange.cultureSurgut.data.Tag

class SportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)
        val tagView: RecyclerView = findViewById(R.id.tagView)
        val linearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val cycleRouteButton :ImageView = findViewById(R.id.routImage)
        cycleRouteButton.setOnClickListener {
            val context = it.context
            val intent = Intent(context, CycleRouteActivity::class.java)
            context.startActivity(intent)
        }
        tagView.layoutManager = linearLayoutManager//LinearLayoutManager(this)
        tagView.adapter = TagsRecyclerAdapter(
            SurgutCultureApplication.db.tagDao().getSports(), baseContext
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    class TagsRecyclerAdapter(private val tags: List<Tag>, val context: Context) :
        RecyclerView.Adapter<TagsRecyclerAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tagTextView: TextView? = null
            var tagImageView: ImageView? = null

            init {
                tagTextView = itemView.findViewById(R.id.textTagName)
                tagImageView = itemView.findViewById(R.id.imageTag)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tagview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tagTextView?.text = tags[position].name
            val bm =
                BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${tags[position].image}.png")
            Log.i("mymy", "result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.tagImageView?.setImageDrawable(d)
            holder.tagImageView?.tag = tags[position].id
            holder.tagImageView?.setOnClickListener {
                val context = it.context
                val intent = Intent(context, ObjectsActivity::class.java)
                intent.putExtra("tagId", it.tag as Int)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return tags.count()
        }

    }

}

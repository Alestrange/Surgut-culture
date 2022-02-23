package ru.alestrange.cultureSurgut

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
import ru.alestrange.cultureSurgut.data.Interest


class InterestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)
        val interestsView: RecyclerView = findViewById(R.id.interestsView)
        interestsView.layoutManager = LinearLayoutManager(this)
        interestsView.adapter = InterestsRecyclerAdapter(SurgutCultureApplication.db.interestDao().getAll(), baseContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this,item)
    }

    class InterestsRecyclerAdapter(private val interests: List<Interest>, val context: Context):
        RecyclerView.Adapter<InterestsRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var interestTextView: TextView? = null
            var interestImageView: ImageView? = null

            init {
                interestTextView = itemView.findViewById(R.id.textInterestName)
                interestImageView = itemView.findViewById(R.id.imageInterest)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.interestsview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.interestTextView?.text = interests[position].name
            val bm = BitmapFactory.decodeFile("${context.filesDir}/$imagePath/${interests[position].image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.interestImageView?.setImageDrawable(d)
            holder.interestImageView?.tag=interests[position].id
            holder.interestImageView?.setOnClickListener {
                val context = it.context
                val intent = Intent(context, ObjectsActivity::class.java)
                intent.putExtra("interestId", it.tag as Int)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return interests.count()
        }

    }
}
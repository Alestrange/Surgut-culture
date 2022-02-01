package ru.alestrange.cultureSurgut

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        findViewById<TextView>(R.id.testTextJ).apply {
            text = SurgutCultureApplication.db.isOpen.toString()
        }
        val interestsView: RecyclerView = findViewById(R.id.interestsView)
        interestsView.layoutManager = LinearLayoutManager(this)
        interestsView.adapter = InterestsRecyclerAdapter(SurgutCultureApplication.db.interestDao().getAll(), baseContext)
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
            Log.i("mymy","result img ${bm.width} ${bm.height}")
            val d: Drawable = BitmapDrawable(context.resources, bm)
            holder.interestImageView?.setImageDrawable(d)
        }

        override fun getItemCount(): Int {
            return interests.count()
        }

    }
}
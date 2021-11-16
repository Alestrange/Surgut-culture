package ru.alestrange.cultureSurgut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InterestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)
        val textView = findViewById<TextView>(R.id.testText).apply {
            text = MyApp.db.isOpen.toString()
        }
        val interestsView: RecyclerView = findViewById(R.id.interestsView)
        interestsView.layoutManager = LinearLayoutManager(this)
        interestsView.adapter = InterestsRecyclerAdapter(MyApp.db.interestDao().getAll())
    }

    class InterestsRecyclerAdapter(private val interests: List<Interest>):
        RecyclerView.Adapter<InterestsRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var largeTextView: TextView? = null
            var smallTextView: TextView? = null

            init {
                largeTextView = itemView.findViewById(R.id.textViewLarge)
                smallTextView = itemView.findViewById(R.id.textViewSmall)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.interestsview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.largeTextView?.text = interests[position].name
            holder.smallTextView?.text = interests[position].imageName
        }

        override fun getItemCount(): Int {
            return interests.count()
        }

    }
}
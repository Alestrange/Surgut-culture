package ru.alestrange.cultureSurgut.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alestrange.cultureSurgut.MainMenu
import ru.alestrange.cultureSurgut.R
import ru.alestrange.cultureSurgut.SurgutCultureApplication
import ru.alestrange.cultureSurgut.data.History
import ru.alestrange.cultureSurgut.databinding.ActivityHistoryEventBinding

private lateinit var binding: ActivityHistoryEventBinding

class HistoryEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val eventView: RecyclerView = binding.eventView
        eventView.layoutManager = LinearLayoutManager(this)
        val periodId = intent?.extras?.getInt("periodId")
        periodId?.let {
            when (periodId)
            {
                1 -> binding.titleText.text=getString(R.string.history_period_1)
                2 -> binding.titleText.text=getString(R.string.history_period_2)
                3 -> binding.titleText.text=getString(R.string.history_period_3)
                4 -> binding.titleText.text=getString(R.string.history_period_4)
                5 -> binding.titleText.text=getString(R.string.history_period_5)
                6 -> binding.titleText.text=getString(R.string.history_period_6)
            }
            val events = SurgutCultureApplication.db.historyDao().getHistoryByPeriod(periodId)
            eventView.adapter = HistoryRecyclerAdapter(events, baseContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    class HistoryRecyclerAdapter(private val events: List<History>, val context: Context):
        RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder>()
    {

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var nameTextView: TextView? = null
            var periodTextView: TextView? = null
            var descriptionTextView: TextView? = null
            var detailButtom: Button? = null
            init {
                nameTextView = itemView.findViewById(R.id.textEventName)
                periodTextView = itemView.findViewById(R.id.textEventPeriod)
                descriptionTextView = itemView.findViewById(R.id.textEventDescription)
                detailButtom = itemView.findViewById(R.id.detailButton)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.eventview_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.nameTextView?.text = events[position].name
            holder.periodTextView?.text = events[position].period
            holder.descriptionTextView?.text = events[position].description
            holder.detailButtom?.tag=events[position].id
            holder.detailButtom?.contentDescription= context.getString(R.string.button_detail_description, events[position].name)
            holder.detailButtom?.setOnClickListener{
                val context = it.context
                val intent = Intent(context, HistoryDetailActivity::class.java)
                intent.putExtra("historyId", it.tag as Int)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return events.count()
        }

    }
}
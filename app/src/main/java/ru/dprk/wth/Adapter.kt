package ru.dprk.wth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var listArray: ArrayList<TaskInfo>, private var context: Context) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userID: TextView = view.findViewById(R.id.user_id)
        private val job: TextView = view.findViewById(R.id.job)

        fun bind(listItem: TaskInfo) {
            userID.text = listItem.userID
            job.text = listItem.job
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listArray = listArray[position]
        holder.bind(listArray)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
}
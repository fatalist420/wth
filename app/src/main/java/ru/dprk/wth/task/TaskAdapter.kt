package ru.dprk.wth.task

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import ru.dprk.wth.R
import ru.dprk.wth.TaskInfo

class TaskAdapter(private var listArray: ArrayList<TaskInfo>, private var context: Context) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userID: TextView = view.findViewById(R.id.user_id)
        private val job: TextView = view.findViewById(R.id.job)

        fun bind(listItem: TaskInfo, context: Context) {
            userID.text = listItem.number
            job.text = listItem.job

            itemView.setOnClickListener(){
                if (listItem.count!! > listItem.progress!!) {
                    FirebaseDatabase.getInstance().reference
                        .child("tasks")
                        .child(listItem.taskID!!)
                        .child("progress")
                        .setValue(listItem.progress?.plus(1))
                }else{
                    MaterialAlertDialogBuilder(context)
                            .setMessage("Задание более не доступно")
                            .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_task_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listArray = listArray[position]
        holder.bind(listArray, context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
}
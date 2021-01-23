package ru.dprk.wth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.dprk.wth.task.NewTaskActivity
import ru.dprk.wth.task.TaskAdapter


class TwoFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_two, container, false)

        val firebaseData: ArrayList<TaskInfo> = ArrayList()
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rec_view)
        val query = FirebaseDatabase.getInstance()
            .reference
            .child("tasks")

        recyclerView.layoutManager = LinearLayoutManager(activity)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseData.clear()
                for (postSnapshot in snapshot.children) {
                    val data = postSnapshot.getValue(TaskInfo::class.java)
                    if (data != null) {
                        firebaseData.add(data)
                    }
                }
                recyclerView.adapter = TaskAdapter(firebaseData)
            }

            override fun onCancelled(error: DatabaseError) {
                //..
            }
        })
        val extendedFab = rootView.findViewById<ExtendedFloatingActionButton>(R.id.extendedFab)
        extendedFab.setOnClickListener {
           startActivity(Intent(activity, NewTaskActivity::class.java))
        }

        return rootView
    }

    companion object {
        fun newInstance(): TwoFragment {
            return TwoFragment()
        }
    }
}
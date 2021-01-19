package ru.dprk.wth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class RecView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_view)

        //добавит загрузку данных виджет

        val firebaseData: ArrayList<TaskInfo> = ArrayList()
        val recyclerView: RecyclerView = findViewById(R.id.rec_view)
        val query = FirebaseDatabase.getInstance()
            .reference
            .child("tasks")

        recyclerView.layoutManager = LinearLayoutManager(this)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseData.clear()
                for (postSnapshot in snapshot.children) {
                    val data = postSnapshot.getValue(TaskInfo::class.java)
                    if (data != null) {
                        firebaseData.add(data)
                    }
                }
                recyclerView.adapter = Adapter(firebaseData, this@RecView)
            }

            override fun onCancelled(error: DatabaseError) {
                //..
            }
        })
    }
}
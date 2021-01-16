package ru.dprk.wth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import ru.dprk.wth.MainActivity.Companion.db


class RecView : AppCompatActivity() {

    lateinit var firebaseData: ArrayList<TaskInfo>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_view)

        recyclerView = findViewById(R.id.rec_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        firebaseData = ArrayList()

        db.addChildEventListener(object : ChildEventListener {
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                firebaseData.clear()
                for (postSnapshot in snapshot.children) {
                    val data = postSnapshot.getValue(TaskInfo::class.java)
                    if (data != null) {
                        firebaseData.add(data)
                    }
                }
                recyclerView.adapter = Adapter(firebaseData, this@RecView)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //...
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //...
            }

            override fun onCancelled(error: DatabaseError) {
                //..
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //...
            }
        })
    }
}
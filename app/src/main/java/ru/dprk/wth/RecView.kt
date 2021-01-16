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

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                firebaseData.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val data = postSnapshot.getValue(TaskInfo::class.java)
                    if (data != null) {
                        firebaseData.add(data)
                    }
                }
                recyclerView.adapter = Adapter(firebaseData, this@RecView)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

        }
    }

//    var adapter: FirebaseRecyclerAdapter<*, *> =
//        object : FirebaseRecyclerAdapter<FirebaseData?, Adapter.ViewHolder?>(options) {
//            // ...
//            override fun onDataChanged() {
//                // Called each time there is a new data snapshot. You may want to use this method
//                // to hide a loading spinner or check for the "no documents" state and update your UI.
//                // ...
//            }
//
//            override fun onError(e: DatabaseError) {
//                // Called when there is an error getting data. You may want to update
//                // your UI to display an error message to the user.
//                // ...
//            }
//
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
//                TOD
//            }
//
//            override fun onBindViewHolder(p0: Adapter.ViewHolder, p1: Int, p2: FirebaseData) {
//                TOD
//            }
//        }



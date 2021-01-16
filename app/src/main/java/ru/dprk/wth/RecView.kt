package ru.dprk.wth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.dprk.wth.MainActivity.Companion.db


class RecView : AppCompatActivity() {

    lateinit var firebaseData: ArrayList<FirebaseData>
    lateinit var recyclerView: RecyclerView
    lateinit var helperAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_view)

        recyclerView = findViewById(R.id.rec_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        firebaseData = ArrayList()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    //val post = dataSnapshot.child("tasks").value
                    //val post1 = dataSnapshot.child("tasks/email").value
                    val data = postSnapshot.child("tasks").getValue(FirebaseData::class.java)
                    if (data != null) {
                        firebaseData.add(data)
                    }


                    //val post1 = dataSnapshot.child("$phoneNumber/email").value
                    //Log.w("TAG", "loadPost: $post")
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



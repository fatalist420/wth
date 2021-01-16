package ru.dprk.wth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class Task : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        //createNewTask(50, "+79118011356", "Прочитать стихотворение Бродского")

    }

//    private fun createNewTask(
//        taskCount: Int,
//        taskTelephone: String,
//        taskJob: String,
//        Action: Boolean = false
//    ): Long {
//        val taskId: Long = System.currentTimeMillis()
//        val taskInfo = TaskInfo(taskCount, taskTelephone, taskJob, Action)
//        Login.db.child("tasks").child(taskId.toString()).setValue(taskInfo)
//        return taskId
//    }
}
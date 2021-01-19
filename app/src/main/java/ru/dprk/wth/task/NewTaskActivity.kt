package ru.dprk.wth.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import ru.dprk.wth.R

class NewTaskActivity : AppCompatActivity() {

    private var targetNumber: String = ""
    private var targetJob: String = ""

    //private val userID = "9118011356"
    private val job = "Fucking Gavin ..."
    private val number = "+79118019988"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        val outlinedTextFieldTargetNumber =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldTargetNumber)
        outlinedTextFieldTargetNumber.editText?.doAfterTextChanged { inputText: Editable? ->
            targetNumber = inputText.toString()
            if (targetNumber != "" && targetNumber.length == 10) {
                outlinedTextFieldTargetNumber.error = null
            }
        }

        val outlinedTextFieldJob = findViewById<TextInputLayout>(R.id.outlinedTextFieldJob)
        outlinedTextFieldJob.editText?.doAfterTextChanged { inputText: Editable? ->
            targetJob = inputText.toString()
            if (targetJob!=""){
                outlinedTextFieldJob.error = null
            }
        }
    }

//    fun createNewTask(){
       //val taskKey = db.child("tasks").key
//        val task = TaskInfo(userID)
//    }

//    fun newTask(){
//        val key = MainActivity.db.child("tasks").push().key
//        Log.d("KEY", key)
//        val newTask =TaskInfo(userID, job, number, 12000)
//        val userlog = TaskLog(userID)
//
//        val map = HashMap<String, Any>()
//        map["/tasks/$key"] = newTask
//        map["/taskslog/$key"] = userlog
//
//        MainActivity.db.updateChildren(map)
//    }
}
package ru.dprk.wth.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import ru.dprk.wth.MainActivity
import ru.dprk.wth.MainActivity.Companion.userID
import ru.dprk.wth.R
import ru.dprk.wth.TaskInfo
import ru.dprk.wth.TaskLog

class NewTaskActivity : AppCompatActivity() {

    private var targetNumber: String = ""
    private var targetJob: String = ""
    private var targetCount: String = ""
    private var targetPrice: Int = 0

    //private val userID = "9118011356"
    //private val job = "Fucking Gavin ..."
    //private val number = "+79118019988"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        val outlinedTextFieldJob = findViewById<TextInputLayout>(R.id.outlinedTextFieldJob)
        outlinedTextFieldJob.editText?.doAfterTextChanged { inputText: Editable? ->
            targetJob = inputText.toString()
            if (targetJob != "") {
                outlinedTextFieldJob.error = null
            }
        }

        val outlinedTextFieldTargetNumber =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldTargetNumber)
        outlinedTextFieldTargetNumber.editText?.doAfterTextChanged { inputText: Editable? ->
            targetNumber = inputText.toString()
            if (targetNumber != "" && targetNumber.length == 10) {
                outlinedTextFieldTargetNumber.error = null
            }
        }

        val outlinedTextFieldTargetCount =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldTargetCount)
        outlinedTextFieldTargetCount.editText?.doAfterTextChanged { inputText: Editable? ->
            targetCount = inputText.toString()
            if (targetCount != "") {
                outlinedTextFieldTargetCount.error = null
            }
        }

        val outlinedTextFieldTargetPrice =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldTargetPrice)
        outlinedTextFieldTargetPrice.editText?.doAfterTextChanged { inputText: Editable? ->
            targetPrice = inputText.toString().toInt()
            if (targetPrice != 0) {
                outlinedTextFieldTargetPrice.error = null
            }
        }

        val buttonCreateNewTask = findViewById<Button>(R.id.button_create_new_task)
        buttonCreateNewTask.setOnClickListener() {
            when (false) {
                targetJob != "" -> outlinedTextFieldJob.error = "Поле не может быть пустым"
                targetNumber != "", targetNumber.length == 10 -> outlinedTextFieldTargetNumber.error =
                    "Не верный формат"
                targetCount != "" -> outlinedTextFieldTargetCount.error =
                    "Поле не может быть пустым"
                targetPrice != 0 -> outlinedTextFieldTargetPrice.error =
                    "Поле не может быть пустым"
                else -> newTask()
            }
        }

    }

//    fun createNewTask(){
    //val taskKey = db.child("tasks").key
//        val task = TaskInfo(userID)
//    }

    private fun newTask() {
        val key: String? = MainActivity.db.child("tasks").push().key
        Log.d("KEY", key!!)
        val newTask = TaskInfo(userID, targetJob, targetNumber, targetPrice)
        val userlog = TaskLog(userID)

        val map = HashMap<String, Any>()
        map["/tasks/$key"] = newTask
        map["/taskslog/$key"] = userlog

        MainActivity.db.updateChildren(map)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    finish()
                }
            }
    }
}
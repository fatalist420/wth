package ru.dprk.wth

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    var REQUEST_CODE_PERMISSION_READ_CONTACTS = 0
    var number: String = ""
    var durat: Long = -1
    var type : Int = -1
    val list = ArrayList<newclass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            xtr()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG), REQUEST_CODE_PERMISSION_READ_CONTACTS)
        }


//        val name: String = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)) // for name
//        val duration: String = c.getString(c.getColumnIndex(CallLog.Calls.DURATION)) // for duration
//        val type: Int = c.getString(c.getColumnIndex(CallLog.Calls.TYPE)).toInt() // for call type, Incoming or out going.

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show()
                    xtr()
                } else {
                    // permission denied
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun xtr() {
        val allCalls: Uri = Uri.parse("content://call_log/calls")
        val vHello = findViewById<TextView>(R.id.hello)

        val cursor = contentResolver.query(allCalls, null, null, null, null)
        if (cursor!!.moveToLast()){
            //var column_index = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            durat = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
            type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
            if (type == 3) {
                list.add(newclass(number, durat, type))
            }
        }
        cursor.close()

        vHello.text = "$number $durat $type"
    }
}


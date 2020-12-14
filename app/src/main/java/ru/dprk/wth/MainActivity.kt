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
    var type: Int = -1
    var date: Long = -1

    val check_number="+79113494010"
    val list = ArrayList<newclass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            xtr(check_number)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                REQUEST_CODE_PERMISSION_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show()
                    xtr(check_number)
                } else {
                    // permission denied
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun xtr(check_number:String) {
        list.clear()
        val allCalls: Uri = Uri.parse("content://call_log/calls")
        val vHello = findViewById<TextView>(R.id.hello)

        val cursor = contentResolver.query(allCalls, null, null, null, null)

        (cursor!!.moveToLast())
        type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
        if (type == CallLog.Calls.OUTGOING_TYPE) {
            number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            durat = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
            date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
            list.add(newclass(number, durat, type, date))
        }

        for (i in 0..50) {
            if (cursor.moveToPrevious()) {
                if (type == CallLog.Calls.OUTGOING_TYPE) {
                    number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    if (check_number == "+79113494010") break
                    durat = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                    date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                    list.add(newclass(number, durat, type, date))
                }
            }
        }
        cursor.close()

        vHello.text = "${list[0]}"
    }

    // Обработка двойного нажатия кнопки "BACK" для выхода из приложения
    private var backPress: Long = 0

    override fun onBackPressed() {
        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            backPress = System.currentTimeMillis()
            Toast.makeText(
                baseContext,
                " Для выхода нажмите кнопку Назад еще раз",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


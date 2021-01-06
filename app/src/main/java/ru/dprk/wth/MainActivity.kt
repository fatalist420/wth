package ru.dprk.wth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var db: DatabaseReference
    }

    var REQUEST_CODE_PERMISSION_READ_CONTACTS = 0
    var number: String = ""
    var durat: Long = -1
    var type: Int = -1
    var date: Long = -1

    private lateinit var loginActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        loginActivity = Intent(this, Login::class.java)
        //запрос на права доступа истории звонков
        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //переход на форму входа/регистрации
            startActivity(loginActivity)
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
                    // permission GRANTED
                    Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show()
                    //переход на форму входа/регистрации
                    startActivity(loginActivity)
                } else {
                    // permission DENIED
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun checkNumber(check_number: String, duration: Int = 0): Boolean {

        val allCalls: Uri = Uri.parse("content://call_log/calls")
        val cursor = contentResolver.query(allCalls, null, null, null, null)

        (cursor!!.moveToLast())
        type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
        for (i in 0..50) {
            if (type == CallLog.Calls.OUTGOING_TYPE) {
                number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                durat = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                if (number == check_number && durat > duration) break
            }
            if (!cursor.moveToPrevious()) break
        }
        cursor.close()
        return number == check_number && durat > duration
    }

    // Обработка двойного нажатия кнопки "BACK" для выхода из приложения
    private var backPress: Long = 0

    override fun onBackPressed() {
        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            auth.signOut()//test
        } else {
            backPress = System.currentTimeMillis()
            Toast.makeText(
                baseContext,
                " Для выхода нажмите еще раз",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


package ru.dprk.wth

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import kotlin.coroutines.*
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.dprk.wth.MainActivity.Companion.auth
import ru.dprk.wth.MainActivity.Companion.db

class Login : AppCompatActivity() {

    private val emailString: String = "@wth.usr"
    private var userID: String = ""
    private var userLogin: String = ""
    private var userPassword: String = ""

    private lateinit var progressWait: ProgressBar

    //test
    private var userWallet: String = "4455258556558554"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressWait = findViewById<ProgressBar>(R.id.progressBar)

        val outlinedTextFieldUserName =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserName)
        outlinedTextFieldUserName.editText?.doAfterTextChanged { inputText: Editable? ->
            userID = inputText.toString()
            userLogin = userID + emailString
            if (userID != "" && userID.length == 10) {
                outlinedTextFieldUserName.error = null
            }
        }

        val outlinedTextFieldUserPassword =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserPassword)
        outlinedTextFieldUserPassword.editText?.doAfterTextChanged { inputText: Editable? ->
            userPassword = inputText.toString()
            if (userPassword.length > 5) {
                outlinedTextFieldUserPassword.error = null
            }
        }

        val outlinedTextFieldUserWallet =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserWallet)
        outlinedTextFieldUserWallet.editText?.doAfterTextChanged { inputText: Editable? ->
            userWallet = inputText.toString()
            if (userWallet.length == 15) {
                outlinedTextFieldUserWallet.error = null
            }
        }

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)    //Кнопка входа
        btnSignIn.setOnClickListener {
            when (false) {
                userID != "", userID.length == 10 -> outlinedTextFieldUserName.error =
                    "Не верный формат"
                userPassword.length > 5 -> outlinedTextFieldUserPassword.error =
                    "Минимум 6 символов"
                else -> {
                    signIn(userLogin, userPassword)
                }
            }
        }

        val btnRegister = findViewById<Button>(R.id.btnRegister)    //Кнопка регистрации
        btnRegister.setOnClickListener {
            outlinedTextFieldUserWallet.visibility = TextInputLayout.VISIBLE
            when (false) {
                userID != "", userID.length == 10 -> outlinedTextFieldUserName.error =
                    "Не верный формат"
                userPassword.length > 5 -> outlinedTextFieldUserPassword.error =
                    "Минимум 6 символов"
                userWallet.length == 15 -> outlinedTextFieldUserWallet.error = "Не верный формат"
                else -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Внимание!!!")
                        .setMessage("Проверьте номер телефона и номер кошелька. Изменить данные будет нельзя!")
                        .setNegativeButton("Cancel") { _, _ ->
                        }
                        .setPositiveButton("Ok") { _, _ ->
                            progressWait.visibility = ProgressBar.VISIBLE
                            createNewUser(userLogin, userPassword)
                        }
                        .show()
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        progressWait.visibility = ProgressBar.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    progressWait.visibility = ProgressBar.INVISIBLE
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Ошибка входа")
                        .setMessage("Проверьте правильность введенных данных или зарегистрируйтесь")
                        .show()
                }
            }
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d("CREATE NEW USER", "SUCCESS")
                    updateProfile()
                }
            }
    }

    private fun writeUserInfo(userID: String, userInfo: UserInfo) {
        db.child("users").child(userID).setValue(userInfo)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("WRITE USER INFO", "SUCCESS")
                    finish()
                }
            }
    }

    private fun updateProfile() {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userID)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("UPDATE PROFILE", "User profile updated.")
                    val userInfo = UserInfo(userWallet)
                    writeUserInfo(userID, userInfo)
                } else {
                    Log.d("UPDATE PROFILE", "NOT")
                }
            }
    }
}
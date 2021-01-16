package ru.dprk.wth

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.dprk.wth.MainActivity.Companion.auth
import ru.dprk.wth.MainActivity.Companion.db


class Login : AppCompatActivity() {

    private val emailString: String = "@wth.usr"
    private var userNumber: String = ""
    private var userLogin: String = ""
    private var userPassword: String = ""

    //test
    private var userWallet: String = "4455258556558554"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val progressWait = findViewById<ProgressBar>(R.id.progressBar)

        val outlinedTextFieldUserName =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserName)
        outlinedTextFieldUserName.editText?.doAfterTextChanged { inputText: Editable? ->
            userNumber = inputText.toString()
            userLogin = userNumber + emailString
            if (userNumber != "" && userNumber.length == 10) {
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
                userNumber != "", userNumber.length == 10 -> outlinedTextFieldUserName.error =
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
                userNumber != "", userNumber.length == 10 -> outlinedTextFieldUserName.error =
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
                            val userInfo = UserInfo(userWallet)
                            writeUserInfo(userNumber, userInfo)
                        }
                        .show()
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
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
                    finish()
                }
            }
    }

    private fun writeUserInfo(userID: String, userInfo: UserInfo) {
        db.child("users").child(userID).setValue(userInfo)
    }

    //для контроля за "users"
    private fun readUserInfo() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.child("users").child(userNumber).value
                Log.w("TAG", "loadPost: $post")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(postListener)
    }
}
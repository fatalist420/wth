package ru.dprk.wth

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
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

        val outlinedTextFieldUserName =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserName)
        outlinedTextFieldUserName.editText?.doAfterTextChanged { inputText: Editable? ->
            userNumber = inputText.toString()
            userLogin = userNumber + emailString
            if (userNumber != "" && userNumber.length == 10) {
                outlinedTextFieldUserName.error = null
            }
            Log.w("SIGN_IN", userLogin)
        }

        val outlinedTextFieldUserPassword =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserPassword)
        outlinedTextFieldUserPassword.editText?.doAfterTextChanged { inputText: Editable? ->
            userPassword = inputText.toString()
            if (userPassword.length > 5) {
                outlinedTextFieldUserPassword.error = null
            } else {
                outlinedTextFieldUserPassword.error = "Минимум 6 символов"
            }
            Log.w("SIGN_IN", userPassword)
        }

        //Кнопка входа
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
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

        //
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            if (userPassword.length > 5) {
                createNewUser(userLogin, userPassword)
            } else {
                outlinedTextFieldUserPassword.error = "Минимум 6 символов"
            }

        }
        //если юзр существует, то войти, если нет, то зарегистрировать и войти

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.currentUser
        //updateUI(currentUser)
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //val currentUser: FirebaseUser? = auth.currentUser
                    //updateUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN_IN", "signUserWithEmail:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val currentUser: FirebaseUser? = auth.currentUser

                    //updateUI(registerFormInfo)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(currentUser)
                }
                // ...
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
        val addValueEventListener = db.addValueEventListener(postListener)
    }
}
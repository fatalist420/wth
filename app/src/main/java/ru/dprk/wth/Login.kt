package ru.dprk.wth

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userName: String = ""
    private var userPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val outlinedTextFieldUserName =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserName)
        outlinedTextFieldUserName.editText?.doAfterTextChanged { inputText: Editable? ->
            if (userName.isEmpty()) {
                outlinedTextFieldUserName.error = "Поле не может быть пустым"
            } else {
                userName = "$inputText@wth.usr"
                Log.w("SIGN_IN", userName)
            }
        }

        val outlinedTextFieldUserPassword =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserPassword)
        outlinedTextFieldUserPassword.editText?.doAfterTextChanged { inputText: Editable? ->
            if (!inputText.isNullOrBlank()) {
                userPassword = inputText.toString()
                outlinedTextFieldUserPassword.error = null
            } else {
                outlinedTextFieldUserPassword.error = "Поле не может быть пустым"
            }

        }

        //Кнопка входа или регистрации
        val btnHelp = findViewById<Button>(R.id.btnHelp)
        btnHelp.setOnClickListener() {
            signIn(userName, userPassword)
        }
        //если юзр существует, то войти, если нет, то зарегистрировать

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.currentUser
        //updateUI(currentUser)
        if (currentUser != null) Toast.makeText(
            this,
            "onStart auth.currentUser",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user: FirebaseUser? = auth.currentUser
                    //updateUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN_IN", "signUserWithEmail:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

    fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user: FirebaseUser? = auth.currentUser
                    //updateUI(currentUser)
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
}
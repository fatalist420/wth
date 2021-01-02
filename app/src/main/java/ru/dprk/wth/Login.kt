package ru.dprk.wth

import android.content.Intent
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
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var db: DatabaseReference
    }

    private val emailString: String = "@wth.usr"
    private var userNumber: String = ""
    private var userLogin: String = userNumber + emailString
    private var userPassword: String = ""

    //test
    private var userWallet: String = "4455258556558554"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val outlinedTextFieldUserName =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserName)
        outlinedTextFieldUserName.editText?.doAfterTextChanged { inputText: Editable? ->
            userNumber = "$inputText"
            Log.w("SIGN_IN", userNumber)
        }

        val outlinedTextFieldUserPassword =
            findViewById<TextInputLayout>(R.id.outlinedTextFieldUserPassword)
        outlinedTextFieldUserPassword.editText?.doAfterTextChanged { inputText: Editable? ->
            userPassword = inputText.toString()
            Log.w("SIGN_IN", userPassword)
        }

        //Кнопка входа
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        btnSignIn.setOnClickListener() {
            signIn(userLogin, userPassword)
        }

        //
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener() {
            createNewUser(userLogin, userPassword)
        }
        //если юзр существует, то войти, если нет, то зарегистрировать

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = auth.currentUser
        //updateUI(currentUser)

        if (currentUser != null) {
            val number = currentUser.displayName
            Toast.makeText(
                this,
                "onStart auth.currentUser $number",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val taskActivity = Intent(this, Task::class.java)
            startActivity(taskActivity)
        }

        //read
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //val currentUser: FirebaseUser? = auth.currentUser
                    writeUserInfo()
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
                    val user: FirebaseUser? = auth.currentUser
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

    private fun writeUserInfo() {
        val userInfo = UserInfo(userWallet)
        db.child("users").child(userNumber).setValue(userInfo)
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
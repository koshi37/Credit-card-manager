package com.example.creditcardmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.creditcardmanager.R
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.User
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private val usernameRegisterInput by lazy { findViewById<TextInputEditText>(R.id.usernameRegisterInput) }
    private val passwordRegisterInput by lazy { findViewById<TextInputEditText>(R.id.passwordRegisterInput) }
    private val confirmPasswordRegisterInput by lazy { findViewById<TextInputEditText>(R.id.passwordRegisterInput) }

    private lateinit var db: DBHelper
    private val activity = this@RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        db = DBHelper(activity)
    }

    fun register(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        val login = usernameRegisterInput.text.toString()
        val password = passwordRegisterInput.text.toString()
        val confirmPassword = confirmPasswordRegisterInput.text.toString()

        if(!db.checkUserExists(login)) {
            db.register(User(0, login, password))
            startActivity(intent)
        }
        else{
            val passwordView = findViewById<TextView>(R.id.usernameTaken).apply {
                isVisible = true
            }
        }
    }

    fun login(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
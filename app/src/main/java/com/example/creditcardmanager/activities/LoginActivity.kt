package com.example.creditcardmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.creditcardmanager.R
import com.google.android.material.textfield.TextInputEditText
import com.example.creditcardmanager.database.DBHelper

class LoginActivity : AppCompatActivity() {

    private val usernameLoginInput by lazy { findViewById<TextInputEditText>(R.id.usernameLoginInput) }
    private val passwordLoginInput by lazy { findViewById<TextInputEditText>(R.id.passwordLoginInput) }
    private lateinit var db: DBHelper
    private val activity = this@LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = DBHelper(activity)
    }

    fun login(view: View){
//        val intent = Intent(this, AddCreditCardActivity::class.java)
        val intent = Intent(this, CreditCardListActivity::class.java)
        val login = usernameLoginInput.text.toString()
        val password = passwordLoginInput.text.toString()
        intent.putExtra("LOGIN", login)
        intent.putExtra("PASSWORD", password)
        var result = db.login(login, password)
        if(result) {
            var userId = db.GetUserID(login)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    fun register(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
package com.example.creditcardmanager.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.creditcardmanager.R
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.session.SessionManager
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private val usernameLoginInput by lazy { findViewById<TextInputEditText>(R.id.usernameLoginInput) }
    private val passwordLoginInput by lazy { findViewById<TextInputEditText>(R.id.passwordLoginInput) }
    private val rememberMeInput by lazy { findViewById<CheckBox>(R.id.rememberMeCheckbox) }
    private lateinit var db: DBHelper
    private val activity = this@LoginActivity
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = DBHelper(activity)

        session = SessionManager(getApplicationContext());
        val user = session.getUserDetails()
        if(user.id != null && user.id != 0 && session.isRemember()) {
            var userId = user.id
            val int = Intent(this, CreditCardListActivity::class.java)
            int.putExtra("USER_ID", userId)
            startActivity(int)
        }
    }

    fun login(view: View){
//        val intent = Intent(this, AddCreditCardActivity::class.java)
        val intent = Intent(this, CreditCardListActivity::class.java)
        val login = usernameLoginInput.text.toString()
        val password = passwordLoginInput.text.toString()
        Log.d("remember", rememberMeInput.isChecked().toString())
//        intent.putExtra("LOGIN", login)
        var result = db.login(login, password)
        if(result) {
            var userId = db.GetUserID(login)
            session?.createLoginSession(login, userId, rememberMeInput.isChecked());
//            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        else {
            val wrongLoginView = findViewById<TextView>(R.id.wrongLoginPass).apply {
                isVisible = true
            }
        }
    }

    fun register(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
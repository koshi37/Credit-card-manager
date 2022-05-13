package com.example.creditcardmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.CreditCard
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddCreditCardActivity : AppCompatActivity() {

    private var login :String?= ""
    private var password :String?= ""
    private lateinit var db: DBHelper
    private val activity = this@AddCreditCardActivity

    private val cardNumberInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardNumberInput) }
    private val cardExpirationInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardExpirationInput) }
    private val cardCvvInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardCvv) }

    private var userId = 0
    private var cardNumber = ""
    private var cardExpiration = ""
    private var cardCvv = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)
        db = DBHelper(activity)
        val login = intent.getStringExtra("LOGIN")
        val password = intent.getStringExtra("PASSWORD")
        userId = intent.getIntExtra("USER_ID", -1)
        val user2 = intent.getStringExtra("USER_ID")
        Log.d("userid", user2.toString())

        val loginView = findViewById<TextView>(R.id.showLogin).apply {
            text = login
        }
        val passwordView = findViewById<TextView>(R.id.showPassword).apply {
            text = password
        }
    }

    fun addCard(view: View){
        cardExpiration = cardExpirationInput.text.toString()
        cardCvv = cardCvvInput.text.toString().toInt()
        cardNumber = cardNumberInput.text.toString()
        Log.d("card", CreditCard(0, userId, cardNumber, cardExpiration, cardCvv).toString())
        db.addCard(CreditCard(0, userId, cardNumber, cardExpiration, cardCvv))
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }

    fun cancel(view: View){
        val users = db.getAllUserCards(userId)
//        Log.d(users)
        Log.d("cards", Arrays.toString(users.toTypedArray()))
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }
}
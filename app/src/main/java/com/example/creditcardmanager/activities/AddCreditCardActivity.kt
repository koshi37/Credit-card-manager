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
import com.example.creditcardmanager.session.SessionManager
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddCreditCardActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private val activity = this@AddCreditCardActivity

    private val cardNumberInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardNumberInput) }
    private val cardExpirationInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardExpirationInput) }
    private val cardCvvInput by lazy { findViewById<TextInputEditText>(R.id.newCreditCardCvv) }

    private lateinit var session: SessionManager

    private var userId = 0
    private var cardNumber = ""
    private var cardExpiration = ""
    private var cardCvv = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)
        db = DBHelper(activity)

        session = SessionManager(applicationContext)
        session.checkLogin()
        val user = session!!.getUserDetails()
        userId = user.id
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
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }
}
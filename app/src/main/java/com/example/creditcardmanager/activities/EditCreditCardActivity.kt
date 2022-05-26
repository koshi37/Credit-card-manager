package com.example.creditcardmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.creditcardmanager.R
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.CreditCard
import com.example.creditcardmanager.session.SessionManager
import com.google.android.material.textfield.TextInputEditText

class EditCreditCardActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private val activity = this@EditCreditCardActivity

    private val cardNumberInput by lazy { findViewById<TextInputEditText>(R.id.editCreditCardNumberInput) }
    private val cardExpirationInput by lazy { findViewById<TextInputEditText>(R.id.editCreditCardExpirationInput) }
    private val cardCvvInput by lazy { findViewById<TextInputEditText>(R.id.editCreditCardCvv) }

    private lateinit var session: SessionManager

    private var userId = 0
    private var cardNumber = ""
    private var cardExpiration = ""
    private var cardCvv = 0
    private var cardId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_credit_card)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        db = DBHelper(activity)

        session = SessionManager(applicationContext)
        session.checkLogin()
        val user = session!!.getUserDetails()
        userId = user.id

        intent = getIntent()
        cardId = intent.getIntExtra("cardId", 0)!!
        cardNumber = intent.getStringExtra("cardNumber")!!
        cardExpiration = intent.getStringExtra("cardExpiration")!!
        cardCvv = intent.getIntExtra("cardCvv", 0)!!

        cardNumberInput.text = Editable.Factory.getInstance().newEditable(cardNumber)
        cardExpirationInput.text = Editable.Factory.getInstance().newEditable(cardExpiration)
        cardCvvInput.text = Editable.Factory.getInstance().newEditable(cardCvv.toString())
    }

    fun editCard(view: View){
        Log.d("edit", CreditCard(cardId, userId, cardExpirationInput.text.toString(), cardNumberInput.text.toString(), cardCvvInput.text.toString().toInt()).toString())
        db.updateCard(CreditCard(cardId, userId, cardExpirationInput.text.toString(), cardNumberInput.text.toString(), cardCvvInput.text.toString().toInt()))
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }

    fun cancel(view: View){
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }
}
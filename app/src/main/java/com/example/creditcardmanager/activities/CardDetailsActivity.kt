package com.example.creditcardmanager.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.creditcardmanager.R
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.CreditCard
import com.example.creditcardmanager.session.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class CardDetailsActivity : AppCompatActivity() {
    private val cardNumber by lazy { findViewById<TextView>(R.id.cardNumberDetails) }
    private val cardExpirationDetails by lazy { findViewById<TextView>(R.id.cardExpirationDetails) }
    private val cardCvvDetails by lazy { findViewById<TextView>(R.id.cardCvvDetails) }

    private val activity = this@CardDetailsActivity
    private lateinit var db: DBHelper
    private lateinit var session: SessionManager
    private lateinit var card: CreditCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_card_details)

        session = SessionManager(applicationContext);
        session.checkLogin()
        db = DBHelper(activity)
        intent = getIntent()
        card = db.getCardDetails(intent.getIntExtra("cardId", -1))!!
        cardNumber.text = card.cardNumber
        cardExpirationDetails.text = card.expiration
        cardCvvDetails.text = card.cvv.toString()
    }

    fun returnToList(view: View) {
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }

    fun shareData(view: View) {
        GlobalScope.launch {
            val data = arrayOf(card.cardNumber, card.expiration, card.cvv.toString())
            val stringToShare = card.cardNumber + " " + card.expiration + " " + card.cvv.toString()
            var shareIntent = Intent().apply() {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, stringToShare)
                this.type = "text/plain"
            }
            var choose = Intent.createChooser(shareIntent, "Share using...")
            startActivity(choose)
        }
    }

    fun deleteCard(view: View) {
        db.deleteCard(card.id)
        val intent = Intent(this, CreditCardListActivity::class.java)
        startActivity(intent)
    }

    fun editCardMenu(view: View) {
        val intent = Intent(this, EditCreditCardActivity::class.java)
        intent.putExtra("cardId", card.id)
        intent.putExtra("cardNumber", card.cardNumber)
        intent.putExtra("cardExpiration", card.expiration)
        intent.putExtra("cardCvv", card.cvv)
        startActivity(intent)
    }
}
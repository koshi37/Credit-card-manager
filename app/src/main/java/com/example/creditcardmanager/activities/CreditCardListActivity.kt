package com.example.creditcardmanager.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.cardlist.CardListAdapter
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.CreditCard
import com.example.creditcardmanager.session.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*


class CreditCardListActivity : AppCompatActivity() {

    private val activity = this@CreditCardListActivity
    private lateinit var db: DBHelper
    private var userId = 0
    private lateinit var session: SessionManager
    private lateinit var cards: List<CreditCard>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_credit_card_list)
        val recyclerview = findViewById<RecyclerView>(R.id.CardListRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        session = SessionManager(applicationContext);
        session.checkLogin()
        val user = session!!.getUserDetails()
        userId = user.id

        db = DBHelper(activity)
        cards = db.getAllUserCards(userId)

        val adapter = CardListAdapter(cards)
        recyclerview.adapter = adapter
    }

    fun addCard(view: View) {
        val intent = Intent(this, AddCreditCardActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    fun logout(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        session.logoutUser()
//        startActivity(intent)
    }

    fun exportCards(view: View) {
        GlobalScope.launch {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val writer = FileOutputStream(File(path, "exported.txt"))
            cards.forEach{
                writer.write("${it.cardNumber};${it.expiration};${it.cvv}\n".toByteArray())
            }
            runOnUiThread {
                Toast.makeText(applicationContext, "Zapisano w pliku exported.txt w folderze Downloads", Toast.LENGTH_LONG)
            }
        }
    }

    fun importCards(view: View) {
        GlobalScope.launch {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val lines = File(path, "exported.txt").readLines()
            for (line in lines) {
                val listString = line.split(';')
                db.addCard(CreditCard(0, userId, listString[0], listString[1], listString[2].toInt()))
            }
            //refresh activity after import
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}
package com.example.creditcardmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.cardlist.CardListAdapter
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.model.CreditCard

class CreditCardListActivity : AppCompatActivity() {

    private val activity = this@CreditCardListActivity
    private lateinit var db: DBHelper
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card_list)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.CardListRecyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel


        // the image with the count of view
        userId = intent.getIntExtra("USER_ID", -1)
        db = DBHelper(activity)
        var data = db.getAllUserCards(userId)

        // This will pass the ArrayList to our Adapter
        val adapter = CardListAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }

    fun addCard(view: View) {
        val intent = Intent(this, AddCreditCardActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    fun logout(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
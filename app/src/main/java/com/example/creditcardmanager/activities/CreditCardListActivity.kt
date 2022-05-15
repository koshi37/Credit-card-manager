package com.example.creditcardmanager.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.cardlist.CardListAdapter
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.session.SessionManager


class CreditCardListActivity : AppCompatActivity() {

    private val activity = this@CreditCardListActivity
    private lateinit var db: DBHelper
    private var userId = 0
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card_list)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.CardListRecyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        session = SessionManager(getApplicationContext());
        session.checkLogin()

        val user = session!!.getUserDetails()
        userId = user.id


        // the image with the count of view
//        userId = intent.getIntExtra("USER_ID", -1)
        db = DBHelper(activity)
        var data = db.getAllUserCards(userId)

        // This will pass the ArrayList to our Adapter
        val adapter = CardListAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        Toast.makeText(getApplicationContext(), "User Login Status: " + session?.getUserDetails().toString(), Toast.LENGTH_LONG).show();
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
}
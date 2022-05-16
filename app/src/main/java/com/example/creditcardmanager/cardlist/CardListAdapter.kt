package com.example.creditcardmanager.cardlist

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.activities.CardDetailsActivity
import com.example.creditcardmanager.model.CreditCard


class CardListAdapter(private val cardList: List<CreditCard>) : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    private lateinit var context: Context

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        context = parent.context;

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cardItem = cardList[position]

        // sets the text to the textview from our itemHolder class
        holder.cardNumberView.text = cardItem.cardNumber
        holder.cardExpirationView.text = cardItem.expiration
        holder.cardCvvView.text = cardItem.cvv.toString()
        holder.cardListItem.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "onClick: clicked on: " + cardItem.id)
            val intent = Intent(context, CardDetailsActivity::class.java)
            intent.putExtra("cardId", cardItem.id)
            context.startActivity(intent)
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return cardList.size
    }

    // Holds the views for adding it to text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardNumberView: TextView = itemView.findViewById(R.id.cardNumber)
        val cardExpirationView: TextView = itemView.findViewById(R.id.cardExpiration)
        val cardCvvView: TextView = itemView.findViewById(R.id.cardCvv)
        val cardListItem: ConstraintLayout = itemView.findViewById(R.id.cardListItem)
    }
}
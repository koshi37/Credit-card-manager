package com.example.creditcardmanager.cardlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.creditcardmanager.R
import com.example.creditcardmanager.model.CreditCard

class CardListAdapter(private val cardList: List<CreditCard>) : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = cardList[position]

        // sets the text to the textview from our itemHolder class
        holder.cardNumberView.text = ItemsViewModel.cardNumber
        holder.cardExpirationView.text = ItemsViewModel.expiration
        holder.cardCvvView.text = ItemsViewModel.cvv.toString()

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
    }
}
package com.example.creditcardmanager.model

data class CreditCard(val id: Int = -1, val userId: Int = -1, val cardNumber: String, val expiration: String, val cvv: Int)
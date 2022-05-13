package com.example.creditcardmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.creditcardmanager.model.CreditCard
import com.example.creditcardmanager.model.User


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // create user table sql query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")
    // drop user table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"
    // create credit card table sql query
    private val CREATE_CREDIT_CARD_TABLE = ("CREATE TABLE " + TABLE_CREDIT_CARD + "("
            + COLUMN_CREDIT_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CREDIT_CARD_NUMBER + " TEXT,"
            + COLUMN_CREDIT_CARD_EXPIRATON + " TEXT,"
            + COLUMN_CREDIT_CARD_CVV + " INTEGER,"
            + COLUMN_USER_ID + " INTEGER REFERENCES " + TABLE_USER +")")
    // drop user table sql query
    private val DROP_CREDIT_CARD_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_CREDIT_CARD_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop Tables if exists
        db.execSQL(DROP_USER_TABLE)
        db.execSQL(DROP_CREDIT_CARD_TABLE)
        // Create tables again
        onCreate(db)
    }

    //TO DO
    /*
    --USER--
    ADD_USER
    CHECK_IF_EXISTS
    LOGIN

    --CREDIT_CARD--
    ADD_CREDIT_CARD
    EDIT_CREDIT_CARD (OPTIONAL)
    REMOVE_CREDIT_CARD (OPTIONAL)
    GET_CARD_NUMBER
    GET_ALL_DATA
     */

    fun register(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.username)
        values.put(COLUMN_USER_PASSWORD, user.password)
        var id = db.insert(TABLE_USER, null, values)
        db.close()
        return id
    }

    fun deleteUser(user: User) {
        val db = this.writableDatabase
        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString()))
        db.close()
    }

    fun checkUserExists(username: String): Boolean {
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase
        val selection = "$COLUMN_USER_NAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USER, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0) {
            return true
        }
        return false
    }

    fun login(username: String, password: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"
        // selection arguments
        val selectionArgs = arrayOf(username, password)
        // query user table with conditions
        val cursor = db.query(TABLE_USER, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0)
            return true
        return false
    }

    fun GetUserID(username: String): Int? {
        val db = this.writableDatabase
        var userId = -1
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER WHERE $COLUMN_USER_NAME = '$username'", null)
        if (cursor.getCount() > 0) {
            cursor.moveToFirst()
            val user = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))
            userId = user
        }
        return userId
    }

    fun getName(username: String, password: String): User? {
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        val selectQuery = ("SELECT * FROM " + TABLE_USER + " WHERE "
                + COLUMN_USER_NAME + " = " + username + " AND "
                + COLUMN_USER_PASSWORD + " = " + password)

        Log.d("selectQuery user", selectQuery)

        val c = db.rawQuery(selectQuery, null)

        c?.moveToFirst()


        val id = c.getInt(c.getColumnIndex(COLUMN_USER_ID)) ?: 0
        val username = c.getString(c.getColumnIndex(COLUMN_USER_NAME)) ?: ""
        return User(id, username, "")
    }

    fun getAllUser(): List<User> {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)
        // sorting orders
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<User>()
        val db = this.readableDatabase
        // query the user table
        val cursor = db.query(TABLE_USER, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    username = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)))
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }


    /*
    CREDIT CARD METHODS
     */

    fun addCard(card: CreditCard) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CREDIT_CARD_NUMBER, card.cardNumber)
        values.put(COLUMN_CREDIT_CARD_CVV, card.cvv)
        values.put(COLUMN_CREDIT_CARD_EXPIRATON, card.expiration)
        values.put(COLUMN_USER_ID, card.userId)
        db.insert(TABLE_CREDIT_CARD, null, values)
        db.close()
    }

    fun getAllUserCards(userId: Int): List<CreditCard> {
        val selectQuery = ("SELECT * FROM " + TABLE_CREDIT_CARD + " WHERE "
                + COLUMN_USER_ID + " = " + userId)
        val db = this.readableDatabase
//        // array of columns to fetch
//        val columns = arrayOf(COLUMN_CREDIT_CARD_ID, COLUMN_CREDIT_CARD_NUMBER, COLUMN_CREDIT_CARD_EXPIRATON, COLUMN_CREDIT_CARD_CVV, COLUMN_USER_ID)
//        // sorting orders
//        val sortOrder = "$COLUMN_CREDIT_CARD_ID ASC"
//
//
//        // query the card table
//        val cursor = db.query(TABLE_CREDIT_CARD, //Table to query
//            columns,            //columns to return
//            null,     //columns for the WHERE clause
//            null,  //The values for the WHERE clause
//            null,      //group the rows
//            null,       //filter by row groups
//            sortOrder)         //The sort order

        val cardList = ArrayList<CreditCard>()
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val card = CreditCard(id = cursor.getString(cursor.getColumnIndex(COLUMN_CREDIT_CARD_ID)).toInt(),
                    userId = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    cardNumber = cursor.getString(cursor.getColumnIndex(COLUMN_CREDIT_CARD_NUMBER)),
                    expiration = cursor.getString(cursor.getColumnIndex(COLUMN_CREDIT_CARD_EXPIRATON)),
                    cvv = cursor.getString(cursor.getColumnIndex(COLUMN_CREDIT_CARD_CVV)).toInt())
                cardList.add(card)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cardList
    }

    companion object {
        // Database Version
        private val DATABASE_VERSION = 1
        // Database Name
        private val DATABASE_NAME = "CreditCardManager.db"
        // User table name
        private val TABLE_USER = "user"
        // User Table Columns names
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "username"
        private val COLUMN_USER_PASSWORD = "password"
        // credit card table name
        private val TABLE_CREDIT_CARD = "credit_card"
        // Credit Card Table Columns names
        private val COLUMN_CREDIT_CARD_ID = "credit_card_id"
        private val COLUMN_CREDIT_CARD_NUMBER = "credit_card_number"
        private val COLUMN_CREDIT_CARD_EXPIRATON = "credit_card_expiration"
        private val COLUMN_CREDIT_CARD_CVV = "credit_card_cvv"
    }
}
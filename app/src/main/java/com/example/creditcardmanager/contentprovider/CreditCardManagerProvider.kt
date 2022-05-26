package com.example.creditcardmanager.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.creditcardmanager.database.DBHelper
import com.example.creditcardmanager.session.SessionManager

class CreditCardManagerProvider: ContentProvider() {

    private lateinit var session: SessionManager
    private lateinit var db: DBHelper

    override fun onCreate(): Boolean{
        session = SessionManager(context!!.applicationContext);
        session.checkLogin()
        db = DBHelper(context!!.applicationContext)
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val user = session!!.getUserDetails()
        val userId = user.id
        return db.getAllUserCardsCursors(userId!!)
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}
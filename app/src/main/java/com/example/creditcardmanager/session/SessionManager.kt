package com.example.creditcardmanager.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.creditcardmanager.activities.CreditCardListActivity
import com.example.creditcardmanager.activities.LoginActivity
import com.example.creditcardmanager.activities.RegisterActivity
import com.example.creditcardmanager.model.User

//https://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/

class SessionManager(  // Context
    var _context: Context) {
    var sharedPref: SharedPreferences
    var editor: SharedPreferences.Editor

    // Shared pref mode
    var PRIVATE_MODE = 0

    companion object {
        private const val PREF_NAME = "pref"
        private const val IS_LOGIN = "IsLoggedIn"
        const val KEY_NAME = "username"
        const val KEY_ID = "uderId"
        const val IS_REMEMBER = "remember"
    }

    init {
        sharedPref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = sharedPref.edit()
    }

    fun createLoginSession(name: String?, id: Int?, remember: Boolean) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_NAME, name)
        editor.putInt(KEY_ID, id!!)
        editor.putBoolean(IS_REMEMBER, remember)
        editor.commit()
    }

    fun isRemember(): Boolean {
        return sharedPref.getBoolean(IS_REMEMBER, false)
    }

//    In order to get the stored preferences data, I added a function called getUserDetails() with the following code.
//    The following function will read shared preferences and returns user data in HashMap
    fun getUserDetails(): User {
        val user = User(sharedPref.getInt(KEY_ID, 0), sharedPref.getString(KEY_NAME, "")?:"", "")
        return user
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(IS_LOGIN, false)
    }

    fun checkLogin()
    {
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            val intent = Intent(_context, LoginActivity::class.java)
            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

        // After logout redirect user to Loing Activity
        val intent = Intent(_context, LoginActivity::class.java)
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login Activity
        _context.startActivity(intent)
    }
}
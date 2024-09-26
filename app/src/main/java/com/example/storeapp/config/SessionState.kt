package com.example.storeapp.config

import android.content.Context
import com.example.storeapp.data.models.CartItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.Reader
import java.io.StringReader


class SessionState private constructor() {
    var isLogin: Boolean = false
    var isFirstHome: Boolean = true
    var username: String = ""
    var emailAddress: String = ""
    var fullname: String = ""
    var cartItems: ArrayList<CartItemData> = arrayListOf()
    var restorationItems: ArrayList<CartItemData> = arrayListOf()

    /** Make cache readable **/
    fun readValuesFromPreferences(context: Context?) {
        if (context != null) {
            val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
            this.isLogin = prefs.getBoolean(AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), false)
            this.isFirstHome = prefs.getBoolean(AppConstants.Companion.PREFERENCES.IS_FIRST_HOME.toString(), true)
            this.username = prefs.getString(AppConstants.Companion.PREFERENCES.USER_ID.toString(), "").toString()
            this.emailAddress = prefs.getString(AppConstants.Companion.PREFERENCES.USER_EMAIL.toString(), "").toString()
            this.fullname = prefs.getString(AppConstants.Companion.PREFERENCES.USER_FULL_NAME.toString(), "").toString()
        }
    }

    /** Cache String values **/
    fun saveValuesToPreferences(context: Context, prefName: String, prefValue: String) {
        val editor = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE).edit()
        editor.putString(prefName, prefValue)
        editor.apply()
    }

    /** Cache boolean values **/
    fun saveBooleanToPreferences(context: Context, prefName: String, prefValue: Boolean) {
        val editor = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE).edit()
        editor.putBoolean(prefName, prefValue)
        editor.apply()
    }

    /** Current cart cache **/
    fun loadUserCart(context: Context?) {
        if (context != null) {
            val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
            val userDataStr =  prefs.getString(AppConstants.Companion.PREFERENCES.USER_CART.toString(), "")
            if (!userDataStr.isNullOrEmpty()) {
                val reader = JsonReader(StringReader(userDataStr) as Reader?)
                reader.isLenient = true
                cartItems = Gson().fromJson(reader, object : TypeToken<ArrayList<CartItemData>>() {}.type)
            }

        }
    }

    /** Cache most recent cart **/
    fun loadUserCartRestore(context: Context?) {
        if (context != null) {
            val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
            val userDataStr =  prefs.getString(AppConstants.Companion.PREFERENCES.USER_CART_RESTORE.toString(), "")
            if (!userDataStr.isNullOrEmpty()) {
                val reader = JsonReader(StringReader(userDataStr) as Reader?)
                reader.isLenient = true
                restorationItems = Gson().fromJson(reader, object : TypeToken<ArrayList<CartItemData>>() {}.type)
            }

        }
    }

    /** Clear session called when Logout **/
    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val TAG = SessionState::class.java.name
        private var sessionState: SessionState? = null

        val instance: SessionState
            get() {
                if (sessionState == null) {
                    sessionState = SessionState()
                }
                return sessionState!!
            }
    }

}

package com.example.praktam_2417051020.data

import android.content.Context
import android.content.SharedPreferences
import com.example.praktam_2417051020.data.model.User
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kamus_it_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString("user", json).apply()
        prefs.edit().putBoolean("is_logged_in", true).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString("user", null)
        return if (json != null) {
            gson.fromJson(json, User::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}

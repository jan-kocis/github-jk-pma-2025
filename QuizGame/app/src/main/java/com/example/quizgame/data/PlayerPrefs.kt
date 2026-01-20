package com.example.quizgame.data

import android.content.Context

object PlayerPrefs {

    private const val PREFS = "player_prefs"
    private const val KEY_NAME = "player_name"

    fun saveName(context: Context, name: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_NAME, name)
            .apply()
    }

    fun loadName(context: Context): String {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_NAME, "") ?: ""
    }
}
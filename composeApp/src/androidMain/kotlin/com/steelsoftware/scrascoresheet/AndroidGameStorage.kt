package com.steelsoftware.scrascoresheet

import android.content.Context
import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class AndroidGameStorage(private val context: Context) : GameStorage {

    private val prefs = context.getSharedPreferences("game_state", Context.MODE_PRIVATE)

    override suspend fun saveGame(json: String) = withContext(Dispatchers.IO) {
        prefs.edit { putString("game", json) }
    }

    override suspend fun loadGame(): String? = withContext(Dispatchers.IO) {
        prefs.getString("game", null)
    }

    override suspend fun clearGame() = withContext(Dispatchers.IO) {
        prefs.edit { remove("game") }
    }
}
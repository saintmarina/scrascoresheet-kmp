package com.steelsoftware.scrascoresheet

import android.content.Context
import androidx.core.content.edit
import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidGameStorage(context: Context) : GameStorage {

    private val prefs = context.getSharedPreferences("game_state", Context.MODE_PRIVATE)

    override suspend fun saveGame(json: String) = withContext(Dispatchers.IO) {
        prefs.edit { putString(GAME_KEY, json) }
    }

    override suspend fun loadGame(): String? = withContext(Dispatchers.IO) {
        prefs.getString(GAME_KEY, null)
    }

    override suspend fun saveGameHistory(historyJson: String) = withContext(Dispatchers.IO) {
        prefs.edit { putString(GAME_HISTORY_KEY, historyJson) }
    }

    override suspend fun loadGameHistory(): String? = withContext(Dispatchers.IO) {
        prefs.getString(GAME_HISTORY_KEY, null)
    }

    override suspend fun clearGame() = withContext(Dispatchers.IO) {
        prefs.edit { remove(GAME_KEY) }
    }

    override suspend fun clearGameHistory() {
        prefs.edit { remove(GAME_HISTORY_KEY) }
    }

    companion object {
        private const val GAME_KEY = "game"
        private const val GAME_HISTORY_KEY = "game_history"
    }
}
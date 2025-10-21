package com.steelsoftware.scrascoresheet

import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSUserDefaults

class IOSGameStorage : GameStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun saveGame(json: String) = withContext(Dispatchers.IO) {
        defaults.setObject(json, forKey = GAME_KEY)
    }

    override suspend fun loadGame(): String? = withContext(Dispatchers.IO) {
        defaults.stringForKey(GAME_KEY)
    }

    override suspend fun saveGameHistory(historyJson: String) = withContext(Dispatchers.IO) {
        defaults.setObject(historyJson, forKey = GAME_HISTORY_KEY)
    }

    override suspend fun loadGameHistory(): String? = withContext(Dispatchers.IO) {
        defaults.stringForKey(GAME_HISTORY_KEY)
    }

    override suspend fun clearGame() = withContext(Dispatchers.IO) {
        defaults.removeObjectForKey(GAME_KEY)
    }

    override suspend fun clearGameHistory() = withContext(Dispatchers.IO) {
        defaults.removeObjectForKey(GAME_HISTORY_KEY)
    }

    companion object {
        private const val GAME_KEY = "game"
        private const val GAME_HISTORY_KEY = "game_history"
    }
}
package com.steelsoftware.scrascoresheet

import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSUserDefaults

class IOSGameStorage : GameStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun saveGame(json: String) = withContext(Dispatchers.Default) {
        defaults.setObject(json, forKey = "game")
    }

    override suspend fun loadGame(): String? = withContext(Dispatchers.Default) {
        defaults.stringForKey("game")
    }

    override suspend fun clearGame() = withContext(Dispatchers.Default) {
        defaults.removeObjectForKey("game")
    }
}
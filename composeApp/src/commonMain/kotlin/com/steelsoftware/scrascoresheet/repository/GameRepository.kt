package com.steelsoftware.scrascoresheet.repository

import com.steelsoftware.scrascoresheet.logic.Game
import com.steelsoftware.scrascoresheet.storage.GameStorage
import kotlinx.serialization.json.Json

class GameRepository(private val storage: GameStorage) {
    suspend fun saveGame(game: Game) {
        val json = Json.encodeToString(game)
        storage.saveGame(json)
    }

    suspend fun loadGame(): Game? {
        val json = storage.loadGame() ?: return null
        return Json.decodeFromString<Game>(json)
    }

    suspend fun saveGameHistory(history: List<Game>) {
        val historyJson = Json.encodeToString(history)
        storage.saveGameHistory(historyJson)
    }

    suspend fun loadGameHistory(): List<Game>? {
        val historyJson = storage.loadGameHistory() ?: return null
        return Json.decodeFromString<List<Game>>(historyJson)
    }

    suspend fun clear() {
        storage.clearGame()
        storage.clearGameHistory()
    }
}
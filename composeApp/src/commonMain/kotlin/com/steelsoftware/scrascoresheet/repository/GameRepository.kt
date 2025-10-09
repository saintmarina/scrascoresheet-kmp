package com.steelsoftware.scrascoresheet.repository

import com.steelsoftware.scrascoresheet.logic.Game
import kotlinx.serialization.json.Json
import com.steelsoftware.scrascoresheet.storage.GameStorage

class GameRepository(private val storage: GameStorage) {

    suspend fun save(game: Game) {
        val json = Json.encodeToString(game)
        storage.saveGame(json)
    }

    suspend fun load(): Game? {
        val json = storage.loadGame() ?: return null
        return Json.decodeFromString<Game>(json)
    }

    suspend fun clear() = storage.clearGame()
}
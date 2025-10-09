package com.steelsoftware.scrascoresheet.storage

interface GameStorage {
    suspend fun saveGame(json: String)
    suspend fun loadGame(): String?
    suspend fun clearGame()
}
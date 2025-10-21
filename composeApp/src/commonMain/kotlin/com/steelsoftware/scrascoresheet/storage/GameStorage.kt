package com.steelsoftware.scrascoresheet.storage

interface GameStorage {
    suspend fun saveGame(json: String)
    suspend fun loadGame(): String?
    suspend fun saveGameHistory(historyJson: String)
    suspend fun loadGameHistory(): String?
    suspend fun clearGame()
    suspend fun clearGameHistory()
}
package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable
import kotlin.collections.plus
import kotlin.math.abs

@Serializable
data class Game(
    val playerNames: List<String>,
    val playersTurns: List<List<Turn>>,
    val currentPlayerIndex: Int,
    val leftOversTurnNumber: Int? = null
) {

    companion object {
        fun createNewGame(playerNames: List<String>): Game {
            val numberOfPlayers = when {
                playerNames.size < 2 -> 2
                else -> playerNames.size.coerceAtMost(4)
            }
            val firstTurn = Turn.Companion.empty()
            val players = List(numberOfPlayers) { listOf(firstTurn) }
            val names = playerNames.take(numberOfPlayers)
            return Game(names,players, 0)
        }

        fun fromPlain(obj: Game) = Game(
            playerNames = obj.playerNames,
            playersTurns = obj.playersTurns,
            currentPlayerIndex = obj.currentPlayerIndex,
            leftOversTurnNumber = obj.leftOversTurnNumber,
        )
    }

    fun addWord(word: Word): Game {
        val currentTurn = getCurrentTurn()
        val newTurn = currentTurn.copy(words = currentTurn.words + word)
        return setTurn(currentPlayerIndex, getCurrentTurnNumber(), newTurn)
    }

    fun endTurn(): Game {
        val filled = if (getCurrentTurn().isEmpty()) this else setTurn(currentPlayerIndex, getCurrentTurnNumber(), getCurrentTurn())
        val nextPlayer = (currentPlayerIndex + 1) % playersTurns.size
        val newPlayers = filled.playersTurns.mapIndexed { idx, turns ->
            if (idx == nextPlayer) turns + Turn.Companion.empty() else turns
        }
        return Game(playerNames, newPlayers, nextPlayer, leftOversTurnNumber)
    }

    fun setBingo(value: Boolean): Game {
        val t = getCurrentTurn().copy(bingo = value)
        return setTurn(currentPlayerIndex, getCurrentTurnNumber(), t)
    }

    fun endGame(): Game = copy(leftOversTurnNumber = getCurrentTurnNumber())

    val isGameOver: Boolean get() = leftOversTurnNumber != null

    fun areLeftOversSubmitted(): Boolean {
        val idx = leftOversTurnNumber ?: return false
        return isGameOver &&
                playersTurns.lastOrNull()?.getOrNull(idx)?.isEmpty() == false &&
                currentPlayerIndex == 0
    }


    fun isMoveInGameOver(move: Int): Boolean =
        isGameOver && leftOversTurnNumber?.let { move >= it } == true


    fun getReapers(): List<Int> {
        val leftOverIndex = leftOversTurnNumber ?: return emptyList()
        return playersTurns.mapIndexedNotNull { index, turns ->
            if (turns.getOrNull(leftOverIndex)?.isEmpty() == true) index else null
        }
    }

    fun getSumOfLeftovers(): Int {
        val leftOverIndex = leftOversTurnNumber ?: return 0
        return playersTurns.sumOf { turns ->
            abs(turns.getOrNull(leftOverIndex)?.score ?: 0)
        }
    }

    fun distributeLeftOversToReapers(reapers: List<Int>, totalLeftOverScore: Int): Game {
        var game = this
        val leftOverIndex = leftOversTurnNumber ?: return this
        for (reaperIndex in reapers) {
            val turn = Turn(
                words = listOf(Word("__reaped_leftovers__", emptyList(), totalLeftOverScore)),
                bingo = false
            )
            game = game.setTurn(reaperIndex, leftOverIndex, turn)
        }
        return game
    }

    fun getWinners(upToMove: Int? = null): List<Int> {
        val totalScores = playersTurns.indices.map { getTotalScore(it, upToMove) }
        val maxScore = totalScores.maxOrNull() ?: 0
        return indexesOf(totalScores, maxScore)
    }

    private fun setTurn(player: Int, turnNumber: Int, turn: Turn): Game {
        val newPlayerTurns = playersTurns[player].toMutableList().apply { this[turnNumber] = turn }
        val updated = playersTurns.mapIndexed { i, t -> if (i == player) newPlayerTurns else t }
        return copy(playersTurns = updated)
    }

    fun getCurrentPlayer(): List<Turn> = playersTurns[currentPlayerIndex]

    fun getCurrentTurn(): Turn = getCurrentPlayer().last()

    fun getCurrentTurnNumber(): Int = playersTurns.first().size - 1

    fun getRunningTotals(playerIndex: Int): List<Int> {
        var total = 0
        val player = playersTurns[playerIndex]
        return player.map {
            total += it.score
            total
        }
    }

    fun getTotalScore(playerIndex: Int, upToMove: Int? = null): Int {
        val totals = getRunningTotals(playerIndex)
        return when {
            upToMove != null -> totals.getOrElse(upToMove) { 0 }
            totals.isEmpty() -> 0
            else -> totals.last()
        }
    }
}
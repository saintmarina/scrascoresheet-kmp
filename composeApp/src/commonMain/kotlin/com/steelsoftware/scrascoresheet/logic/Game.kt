package com.steelsoftware.scrascoresheet.logic

import kotlinx.serialization.Serializable
import kotlin.collections.plus
import kotlin.math.abs

@Serializable
data class Game(
    val playersTurns: List<List<Turn>>,
    val currentPlayerIndex: Int,
    val leftOversTurnNumber: Int? = null
) {

    companion object {
        fun createNewGame(numberOfPlayers: Int): Game {
            val firstTurn = Turn.Companion.empty()
            val players = List(numberOfPlayers) { listOf(firstTurn) }
            return Game(players, 0)
        }

        fun fromPlain(obj: Game) = Game(
            playersTurns = obj.playersTurns,
            currentPlayerIndex = obj.currentPlayerIndex,
            leftOversTurnNumber = obj.leftOversTurnNumber
        )
    }

    fun addWord(word: Word): Game {
        val currentTurn = getCurrentTurn()
        val newTurn = currentTurn.copy(words = currentTurn.words + word)
        return setTurn(currentPlayerIndex, getCurrentTurnNumber(), newTurn)
    }

    fun endTurn(): Game {
        val filled = if (getCurrentTurn().isEmpty) this else setTurn(currentPlayerIndex, getCurrentTurnNumber(), getCurrentTurn())
        val nextPlayer = (currentPlayerIndex + 1) % playersTurns.size
        val newPlayers = filled.playersTurns.mapIndexed { idx, turns ->
            if (idx == nextPlayer) turns + Turn.Companion.empty() else turns
        }
        return Game(newPlayers, nextPlayer, leftOversTurnNumber)
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
                playersTurns.lastOrNull()?.getOrNull(idx)?.isEmpty == false &&
                currentPlayerIndex == 0
    }

    fun getReapers(): List<Int> {
        val idx = leftOversTurnNumber ?: return emptyList()
        return playersTurns.indices.filter { playersTurns[it][idx].isEmpty }
    }

    fun getSumOfLeftovers(): Int {
        val idx = leftOversTurnNumber ?: return 0
        return playersTurns.sumOf { abs(it[idx].score()) }
    }

    fun distributeLeftOversToReapers(reapers: List<Int>, total: Int): Game {
        var g = this
        reapers.forEach { i ->
            val turn = Turn(listOf(Word("__reaped_leftovers__", emptyList(), total)))
            g = g.setTurn(i, leftOversTurnNumber!!, turn)
        }
        return g
    }

    fun getWinners(upToMove: Int? = null): List<Int> {
        val totals = playersTurns.indices.map { getTotalScore(it, upToMove) }
        val max = totals.maxOrNull() ?: 0
        return totals.withIndex().filter { it.value == max }.map { it.index }
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
            total += it.score()
            total
        }
    }

    fun getTotalScore(playerIndex: Int, upToMove: Int? = null): Int {
        val totals = getRunningTotals(playerIndex)
        return upToMove?.let { totals.getOrNull(it) ?: 0 } ?: totals.lastOrNull() ?: 0
    }
}
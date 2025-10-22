package com.steelsoftware.scrascoresheet.ui.game

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.steelsoftware.scrascoresheet.AnalyticsManager
import com.steelsoftware.scrascoresheet.logic.ModifierType
import com.steelsoftware.scrascoresheet.logic.Turn
import com.steelsoftware.scrascoresheet.logic.Word
import com.steelsoftware.scrascoresheet.logic.scoreListsMap
import com.steelsoftware.scrascoresheet.repository.GameRepository
import com.steelsoftware.scrascoresheet.ui.game.GameState.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import com.steelsoftware.scrascoresheet.logic.Game as GameObj


@OptIn(ExperimentalTime::class)
class GameComponent(
    componentContext: ComponentContext,
    private val gameRepository: GameRepository,
    private val game: GameObj,
    private val onStartNewGame: () -> Unit,
    private val analytics: AnalyticsManager,
) : ComponentContext by componentContext {
    private val _state = MutableValue<GameState>(GameState.Loading)
    val state: Value<GameState> = _state
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        doOnResume {
            scope.launch {
                val game = gameRepository.loadGame() ?: this@GameComponent.game
                val history = gameRepository.loadGameHistory().orEmpty()
                _state.value = Game(game, history)
            }
        }
    }

    fun endGame() {
        val currentState = _state.value
        if (currentState !is Game) return

        val newGame = currentState.game.startLeftOvers()
        val newGameHistory = currentState.gameHistory + newGame

        analytics.logEvent("end_game", loggableGame(newGame))

        scope.launch {
            gameRepository.saveGame(newGame)
            gameRepository.saveGameHistory(newGameHistory)
            _state.value = Game(
                game = newGame,
                gameHistory = newGameHistory
            )
        }
    }

    fun startNewGame() {
        analytics.logEvent("start_new_game")
        scope.launch {
            gameRepository.clear()
            onStartNewGame()
        }
    }

    fun calculateScrabbleScore(
        word: String,
        modifiers: List<ModifierType>,
        language: String
    ): Int {
        val scores = scoreListsMap[language] ?: return 0

        var result = 0
        var wordMultiplier = 1

        word.forEachIndexed { i, letter ->
            val modifier = modifiers.getOrNull(i) ?: ModifierType.NONE
            var score = scores[letter.lowercaseChar()] ?: 0

            when (modifier) {
                ModifierType.BLANK -> {
                    score = 0
                }

                ModifierType.DOUBLE_LETTER -> score *= 2
                ModifierType.TRIPLE_LETTER -> score *= 3
                else -> Unit
            }

            result += score

            when (modifier) {
                ModifierType.DOUBLE_WORD -> wordMultiplier *= 2
                ModifierType.TRIPLE_WORD -> wordMultiplier *= 3
                else -> Unit
            }
        }

        return result * wordMultiplier
    }

    /**
     * Ends the current player's turn and advances the game to the next player.
     * Also used to PASS turn.
     *
     * If the provided [currentWord] is not empty, it is first added to the
     * current turn using [GameObj.addWord]. The game state is then advanced
     * by calling [GameObj.endTurn].
     *
     * The previous [Game] instance is preserved in [GameState.Game.gameHistory]
     * to enable undo functionality.
     *
     * @param currentWord The word currently being entered by the active player.
     */
    fun endTurn(currentWord: Word) {
        val currentState = _state.value
        if (currentState !is Game) return

        var game = currentState.game

        if (currentWord.value.isNotBlank()) {
            game = game.addWord(currentWord)
        }

        val newGame = game.endTurn()
        val newGameHistory = currentState.gameHistory + currentState.game

        val eventData = if (currentWord.value.isNotBlank()) {
            mapOf("word" to loggableWord(currentWord))
        } else {
            mapOf("passed" to true)
        }
        analytics.logEvent("end_turn", eventData)

        scope.launch {
            gameRepository.saveGame(newGame)
            gameRepository.saveGameHistory(newGameHistory)
            _state.value = Game(
                game = newGame,
                gameHistory = newGameHistory,
            )
        }
    }

    /**
     * Calls [GameObj.addWord] and saves the new GameState
     */
    fun addWord(currentWord: Word) {
        val currentState = _state.value
        if (currentState !is Game) return
        val game = currentState.game

        // Add a new word to current turn
        val newGame = game.addWord(currentWord)
        val newGameHistory = currentState.gameHistory + game

        analytics.logEvent("add_word", mapOf("word" to loggableWord(currentWord)))

        scope.launch {
            gameRepository.saveGame(newGame)
            gameRepository.saveGameHistory(newGameHistory)
            _state.value = Game(newGame, newGameHistory)
        }
    }

    /**
     * Toggles the bingo flag for the current player's turn.
     *
     * If bingo is enabled, a 50-point bonus will later apply
     * to this turn’s total score. The updated game state is
     * stored along with the previous snapshot for undo support.
     */
    fun toggleBingo() {
        val currentState = _state.value
        if (currentState !is Game) return
        val game = currentState.game

        val isBingo = !game.getCurrentTurn().bingo
        val newGame = game.setBingo(isBingo)
        val newGameHistory = currentState.gameHistory + game

        analytics.logEvent("toggle_bingo")

        scope.launch {
            gameRepository.saveGame(newGame)
            gameRepository.saveGameHistory(newGameHistory)
            _state.value = Game(newGame, newGameHistory)
        }
    }

    /**
     * Reverts the game state to the previous turn.
     *
     * If a previous state exists in [Game.gameHistory], it restores the most recent
     * game snapshot and removes it from history. Has no effect if there is no
     * previous game recorded.
     */
    fun undo() {
        val currentState = _state.value
        if (currentState !is Game) return

        if (currentState.gameHistory.isNotEmpty()) {
            val previousHistory = currentState.gameHistory.dropLast(1)
            val previousGame = currentState.gameHistory.last()

            analytics.logEvent("undo")

            scope.launch {
                gameRepository.saveGame(previousGame)
                gameRepository.saveGameHistory(previousHistory)
                _state.value = Game(previousGame, previousHistory)
            }
        }
    }

    fun submitLeftovers(currentWord: Word) {
        val currentState = _state.value
        if (currentState !is Game) return
        var game = currentState.game

        if (currentWord.value.isNotBlank()) {
            game = game.addWord(currentWord)
        }

        game = game.endTurn()

        if (game.currentPlayerIndex == 0) {
            game = game.distributeLeftOversToReapers(
                game.getReapers(),
                game.getSumOfLeftovers()
            )
        }

        val newGameHistory = currentState.gameHistory + currentState.game

        val eventData = currentWord.value.ifBlank { "No leftovers submitted" }
        analytics.logEvent("submit_leftovers", mapOf("leftovers" to eventData))

        scope.launch {
            gameRepository.saveGame(game)
            gameRepository.saveGameHistory(newGameHistory)
            _state.value = Game(
                game = game,
                gameHistory = newGameHistory
            )
        }
    }

    private fun loggableWord(word: Word): String {
        val modifiers = word.modifiers.mapIndexedNotNull { i, mod ->
            val modName = when (mod) {
                ModifierType.NONE -> null
                else -> "$i:${mod.name}"
            }
            modName
        }.joinToString(", ")

        return if (modifiers.isEmpty()) {
            word.value
        } else {
            "${word.value} ($modifiers)"
        }
    }

    private fun loggableTurn(turn: Turn): String =
        when {
            turn.isPassed(game) -> "PASS"
            turn.words.isNotEmpty() -> {
                val turnWords = turn.words.joinToString("+") { loggableWord(it) }
                if (turn.bingo) "$turnWords BINGO" else turnWords
            }

            else -> ""
        }

    @OptIn(ExperimentalTime::class)
    private fun loggableGame(game: GameObj): Map<String, Any> {
        val turns = game.playersTurns.map { playerTurns ->
            playerTurns.joinToString(",") { loggableTurn(it) }
        }

        val scores = game.playersTurns.mapIndexed { i, _ -> game.getTotalScore(i) }
        val numTurns = game.playersTurns.firstOrNull()?.size?.minus(1) ?: 0

        val endTimeMillis = Clock.System.now().toEpochMilliseconds()
        val durationMins = (endTimeMillis - game.startTime).toDouble() / 1000.0 / 60.0

        return buildMap {
            put("turns", turns)
            put("scores", scores)
            put("numTurns", numTurns)
            put("durationMins", durationMins)
        }
    }
}
import java.util.*
import kotlin.random.Random

interface Player {
    fun guessNumber(): Int
    fun provideFeedback(guess: Int, feedback: String)
    fun startNewGame()
}

class HumanPlayer : Player {
    override fun guessNumber(): Int {
        println("Введіть ваше припущення:")
        return readlnOrNull()?.toIntOrNull() ?: -1
    }

    override fun provideFeedback(guess: Int, feedback: String) {
        println(feedback)
    }

    override fun startNewGame() {
        println("Гра почалася!\nВгадайте число від 0 до 100.")
    }
}

class ComputerPlayer : Player {
    private var min = 0
    private var max = 100
    private var currentGuess = -1

    override fun guessNumber(): Int {
        currentGuess = (min + max) / 2
        println("Комп'ютер припускає: $currentGuess")
        return currentGuess
    }

    override fun provideFeedback(guess: Int, feedback: String) {
        when (feedback) {
            "більше" -> min = guess + 1
            "менше" -> max = guess - 1
        }
    }

    override fun startNewGame() {
        min = 0
        max = 100
        println("Комп'ютер починає нову гру.\nЗагадайте число та натисніть Enter")
        readlnOrNull()
    }

    fun requestFeedback(): String {
        println("Ваше число більше, менше або дорівнює $currentGuess? (введіть: більше, менше або дорівнює)")
        return readlnOrNull()?.trim()?.lowercase(Locale.getDefault()) ?: ""
    }
}

class Game(human: Player, computer: Player) {
    private var secretNumber = 0
    private var currentPlayer: Player = human
    private var opponentPlayer: Player = computer

    fun start() {
        while (true) {
            if (currentPlayer is HumanPlayer) {
                secretNumber = Random.nextInt(101)
                currentPlayer.startNewGame()
                var guess: Int
                do {
                    guess = currentPlayer.guessNumber()
                    when {
                        guess == secretNumber -> {
                            println("Правильно! Число було $secretNumber.")
                            swapPlayers()
                            break
                        }
                        guess < secretNumber -> currentPlayer.provideFeedback(guess, "Загадане число більше")
                        guess > secretNumber -> currentPlayer.provideFeedback(guess, "Загадане число менше")
                    }
                } while (true)
            } else if (currentPlayer is ComputerPlayer) {
                (currentPlayer as ComputerPlayer).startNewGame()
                var feedback: String
                do {
                    val guess = currentPlayer.guessNumber()
                    feedback = (currentPlayer as ComputerPlayer).requestFeedback()
                    when (feedback) {
                        "дорівнює" -> {
                            println("Комп'ютер правильно вгадав число $guess!")
                            swapPlayers()
                            break
                        }
                        "більше" -> currentPlayer.provideFeedback(guess, "більше")
                        "менше" -> currentPlayer.provideFeedback(guess, "менше")
                        else -> println("Невірне введення. Будь ласка, введіть більше, менше або дорівнює.")
                    }
                } while (true)
            }
        }
    }

    private fun swapPlayers() {
        val temp = currentPlayer
        currentPlayer = opponentPlayer
        opponentPlayer = temp
    }
}

fun main() {
    val humanPlayer = HumanPlayer()
    val computerPlayer = ComputerPlayer()
    val game = Game(humanPlayer, computerPlayer)
        game.start()
}

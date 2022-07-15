package indigo

enum class ActionPlayer {
    RESET,
    SHUFFLE,
    GET,
    EXIT;
}

class Indigo {
    private val cardSuits = listOf("♣", "♦", "♥", "♠")
    private val cardRanks = listOf("K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2", "A")
    private var cards = mutableListOf<String>()

    init {
        resetCards()
        play()
    }

    private fun play() {
        var playerActionInput = ""
        while (playerActionInput != ActionPlayer.EXIT.name) {
            println("Choose an action (reset, shuffle, get, exit):")
            playerActionInput = readln().trim().uppercase()
            when (playerActionInput) {
                ActionPlayer.RESET.name -> {
                    resetCards()
                    println("Card deck is reset.")
                }
                ActionPlayer.SHUFFLE.name -> {
                    cards.shuffle()
                    println("Card deck is shuffled.")
                }
                ActionPlayer.GET.name -> getCards()
                ActionPlayer.EXIT.name -> println("Bye")
                else -> println("Wrong action.")
            }
        }
    }

    private fun resetCards() {
        var index = 0
        cards.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cards.add(index, "$rank$suit")
                index++
            }
        }
    }

    private fun getCards() {
        println("Number of cards:")
        val input = readln()
        if (!input.matches(Regex("\\d+")) || input.toInt() !in 1..52) {
            println("Invalid number of cards.")
        } else {
            if (cards.size >= input.toInt()) {
                for (i in 0 until input.toInt()) {
                    print("${cards[0]} ")
                    cards.removeAt(0)
                }
                println()
            } else {
                println("The remaining cards are insufficient to meet the request.")
            }
        }
    }
}

fun main() {
    Indigo()
}

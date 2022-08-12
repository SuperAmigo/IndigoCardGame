package indigo

class Indigo {
    private val cardSuits = listOf("♣", "♦", "♥", "♠")
    private val cardRanks = listOf("K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2", "A")
    private var cardDeck = mutableListOf<String>()
    private var cardsOnTable = mutableListOf<String>()
    private var cardsPlayer = mutableListOf<String>()
    private var cardsComputer = mutableListOf<String>()
    private var isPlayerTurn = false

    init {
        println("Indigo Card Game")
        askForPlayFirst()
        initialCard()
        play()
    }

    private fun initialCard() {
        resetCards()
        cardDeck.shuffle()
        print("Initial cards on the table: ")
        cardsOnTable = getCards(4)
        println(cardsOnTable.joinToString(" "))

    }

    private fun askForPlayFirst() {
        val correctAnswer = arrayOf("yes", "no")
        var answer: String
        do {
            println("Play first?")
            answer = readln()
        } while (answer !in correctAnswer)
        isPlayerTurn = answer == "yes"
    }

    private fun play() {
        var playerActionInput = ""
        while (playerActionInput != "exit" && cardsOnTable.size < 52) {
            if (cardsComputer.size != 0 || cardsPlayer.size != 0) {
                cardOnTableAndTop()
                if (isPlayerTurn) playerActionInput = playerTurn() else computerTurn()
            } else {
                cardsComputer = getCards(6)
                cardsPlayer = getCards(6)
            }
        }
        if (cardsOnTable.size == 52) cardOnTableAndTop()
        println("Game Over")
    }

    private fun computerTurn() {
        println("Computer plays ${cardsComputer[0]}")
        cardsOnTable.add(cardsComputer[0])
        cardsComputer.removeAt(0)
        isPlayerTurn = true
    }

    private fun playerTurn(): String {
        print("Cards in hand:")
        for (i in 0 until cardsPlayer.size) {
            print(" ${i+1})${cardsPlayer[i]}")
        }
        println()
        var input: String
        do {
            println("Choose a card to play (1-${cardsPlayer.size}):")
            input = readln()
            if (input.lowercase() == "exit") return "exit"
            val isCorrect = input.matches(Regex("\\d+")) && input.toInt() in 1..cardsPlayer.size
        } while (!isCorrect)
        cardsOnTable.add(cardsPlayer[input.toInt() - 1])
        cardsPlayer.removeAt(input.toInt() - 1)
        isPlayerTurn = false
        return input
    }

    private fun cardOnTableAndTop() {
        println("\n${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable[cardsOnTable.lastIndex]}")
    }

    private fun resetCards() {
        var index = 0
        cardDeck.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cardDeck.add(index, "$rank$suit")
                index++
            }
        }
    }

    private fun getCards(cardCount: Int): MutableList<String> {
        val cards = mutableListOf<String>()
        if (cardCount <= cardDeck.size) {
            for (i in 0 until cardCount) {
                cards.add(cardDeck[0])
                cardDeck.removeAt(0)
            }
        } else {
            println("The remaining cards are insufficient to meet the request.")
        }
        return cards
    }
}

fun main() {
    Indigo()
}

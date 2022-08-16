package indigo

class Indigo {
    private val cardSuits = listOf("♣", "♦", "♥", "♠")
    private val cardRanks = listOf("K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2", "A")
    private val cardWithPointRanks = listOf('K', 'Q', 'J', '1', 'A')
    private var cardDeck = mutableListOf<String>()
    private var cardsTable = mutableListOf<String>()
    private var cardsPlayer = mutableListOf<String>()
    private var cardsComputer = mutableListOf<String>()
    private var isPlayerTurn = false
    private var cardsCount = 52
    private var playerScore = 0
    private var playerWinCards = 0
    private var compScore = 0
    private var compWinCards = 0

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
        cardsTable = getCards(4)
        cardsCount -= 4
        println(cardsTable.joinToString(" "))

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
        while (playerActionInput != "exit" && cardsCount >= 1) {
            if (cardsComputer.size != 0 || cardsPlayer.size != 0) {
                cardOnTableAndTop()
                if (isPlayerTurn) playerActionInput = playerTurn() else computerTurn()
                cardsCount--
                if (cardsTable.size > 1) {
                    checkTurn()
                }
                isPlayerTurn = !isPlayerTurn
            } else {
                cardsComputer = getCards()
                cardsPlayer = getCards()
            }
        }
        if (cardsCount == 0) cardOnTableAndTop()
        println("Game Over")
    }

    private fun checkTurn() {
        val currentCardRank =cardsTable[cardsTable.lastIndex].replace(("[^\\w\\d ]").toRegex(), "")
        val previousCardRank = cardsTable[cardsTable.lastIndex - 1].replace(("[^\\w\\d ]").toRegex(), "")

        val currentCardSuit = cardsTable[cardsTable.lastIndex][1]
        val previousCardSuit = cardsTable[cardsTable.lastIndex - 1][1]

        if (currentCardRank == previousCardRank || currentCardSuit == previousCardSuit) {
            calculateScore()
            printWinnerAndScore()
            cardsTable.clear()
            getCards()

        }
    }

    private fun calculateScore() {

        if (isPlayerTurn) {
            playerWinCards += cardsTable.size
            playerScore += calculateCardsPoint()
        } else {
            compWinCards += cardsTable.size
            compScore += calculateCardsPoint()
        }
    }

    private fun calculateCardsPoint(): Int {
        var count = 0
        for (i in 0 until cardsTable.size) {
            if (cardsTable[i][0] in cardWithPointRanks) count++
        }
        return count
    }

    private fun printWinnerAndScore() {
        println(if (isPlayerTurn) "Player wins cards" else "Computer wins cards")
        println("Score: Player $playerScore - Computer $compScore")
        println("Cards: Player $playerWinCards - Computer $compWinCards")
    }

    private fun computerTurn() {
        println("Computer plays ${cardsComputer[0]}")
        cardsTable.add(cardsComputer[0])
        cardsComputer.removeAt(0)
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
        cardsTable.add(cardsPlayer[input.toInt() - 1])
        cardsPlayer.removeAt(input.toInt() - 1)
        return input
    }

    private fun cardOnTableAndTop() {
        if (cardsTable.size == 0) {
            println("\nNo cards on the table")
        } else {
            println("\n${cardsTable.size} cards on the table, and the top card is ${cardsTable[cardsTable.lastIndex]}")
        }
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

    private fun getCards(cardCount: Int = 6): MutableList<String> {
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

package indigo

private val cardSuits = listOf("♣", "♦", "♥", "♠")
private val cardRanks = listOf("K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2", "A")

data class Card (val rank: String, val suit: String) {
    override fun toString() = "$rank$suit"
}

class Deck {
    private val cardDeck = mutableListOf<Card>()

    fun initDeck() {
        cardDeck.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cardDeck.add(Card(rank, suit))
            }
        }
    }

    fun getCards(numberCards: Int = 6): MutableList<Card> {
        val listCard = mutableListOf<Card>()
        if (numberCards <= cardDeck.size) {
            for (i in 0 until numberCards) {
                listCard.add(cardDeck[0])
                cardDeck.removeAt(0)
            }
        } else {
            println("The remaining cards are insufficient to meet the request.")
        }
        return listCard
    }

    fun shuffleCards() = cardDeck.shuffle()
}

class Player() {
    var handCards = mutableListOf<Card>()
    var winCards = mutableListOf<Card>()
    var score = 0
}

class Table {
    var tableCards = mutableListOf<Card>()
}

class Indigo {
    private val cardsRanksWinPoint = listOf("K", "Q", "J", "10", "A")
    private var isHumanTurn = false
    private var isHumanLastWin = false
    private var cardsCount = 52
    private val cardDeck = Deck()
    private val table = Table()
    private val human = Player()
    private val computer = Player()

    private fun initialGame() {
        println("Indigo Card Game")
        val correctAnswer = arrayOf("yes", "no")
        var answer: String
        do {
            println("Play first?")
            answer = readln()
        } while (answer !in correctAnswer)
        isHumanTurn = answer == "yes"
        isHumanLastWin = isHumanTurn
    }

    private fun initialCard() {
        cardDeck.initDeck()
        cardDeck.shuffleCards()
        print("Initial cards on the table: ")
        table.tableCards = cardDeck.getCards(4)
        cardsCount -= 4
        println(table.tableCards.joinToString(" "))
    }

    fun play() {
        initialGame()
        initialCard()
        var playerActionInput = ""
        while (playerActionInput != "exit" && cardsCount >= 1) {
            if (human.handCards.size != 0 || computer.handCards.size != 0) {
                cardOnTableAndTop()
                if (isHumanTurn) playerActionInput = playerTurn() else computerTurn()
                cardsCount--
                if (table.tableCards.size > 1) {
                    checkTurn()
                }
                isHumanTurn = !isHumanTurn
            } else {
                human.handCards = cardDeck.getCards(6)
                computer.handCards = cardDeck.getCards(6)
            }
        }
        if (cardsCount == 0) {
            cardOnTableAndTop()
            if (human.winCards.size > computer.winCards.size)
                human.score += 3
            else
                computer.score += 3
            if (isHumanLastWin)
                calculateScore(human)
            else
                calculateScore(computer)
            printScore()
        }
        println("Game Over")
    }

    private fun checkTurn() {
        val topCard = table.tableCards[table.tableCards.lastIndex]
        val previousCard = table.tableCards[table.tableCards.lastIndex - 1]
        if (topCard.rank == previousCard.rank || topCard.suit == previousCard.suit) {
            isHumanLastWin = if (isHumanTurn) {
                println("Player wins cards")
                calculateScore(human)
                true
            } else {
                println("Computer wins cards")
                calculateScore(computer)
                false
            }
            printScore()
            table.tableCards.clear()
        }
    }

    private fun calculateScore(player: Player) {
        player.winCards += table.tableCards
        player.score += calculateCardsPoint()
    }

    private fun calculateCardsPoint(): Int {
        var count = 0
        for (i in 0 until table.tableCards.size) {
            if (table.tableCards[i].rank in cardsRanksWinPoint) count++
        }
        return count
    }

    private fun printScore() {
        println("Score: Player ${human.score} - Computer ${computer.score}")
        println("Cards: Player ${human.winCards.size} - Computer ${computer.winCards.size}")
    }

    private fun computerTurn() {
        val computerPlay = computer.handCards[0]
        println("Computer plays $computerPlay")
        table.tableCards.add(computerPlay)
        computer.handCards.remove(computerPlay)
    }

    private fun playerTurn(): String {
        print("Cards in hand:")
        for (i in 0 until human.handCards.size) {
            print(" ${i+1})${human.handCards[i]}")
        }
        var input: String
        do {
            println("\nChoose a card to play (1-${human.handCards.size}):")
            input = readln()
            if (input.lowercase() == "exit") return "exit"
            val isCorrect = input.matches(Regex("\\d+")) && input.toInt() in 1..human.handCards.size
        } while (!isCorrect)
        table.tableCards.add(human.handCards[input.toInt() - 1])
        human.handCards.removeAt(input.toInt() - 1)
        return input
    }

    private fun cardOnTableAndTop() {
        val size = table.tableCards.size
        if (size == 0) {
            println("\nNo cards on the table")
        } else {
            println("\n${size} cards on the table, and the top card is ${table.tableCards[size - 1]}")
        }
    }
}

fun main() {
    Indigo().play()
}
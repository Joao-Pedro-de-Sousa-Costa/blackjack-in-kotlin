package src.main.kotlin

class Game {
    private var wins = 0 // numero de vitorias
    private var losses = 0 // numero de derrotas
    private var pushes = 0 // Numero de empates
    private val player1 = Player()
    //private val player2
    //private val player3
    private val deck = Card.createDeck()
    private val dealer = Dealer()

    init {

        deck.shuffle()
        startRound()
    }


    private fun startRound() {

        dealer.getHand().dealCard(deck)
        dealer.getHand().dealCard(deck)

        player1.getHand().dealCard(deck)
        player1.getHand().dealCard(deck)

        dealer.printFirstHand()
        println(player1.getHand())

        println(deck.size)


    }
}

package src.main.kotlin

class Card(private val suit: Suit, private val rank: Rank) {
    fun getValue() = rank.rankValue
    fun getSuit() = suit
    fun getRank() = rank

    override fun toString(): String {
        return "[$rank of $suit] (${getValue()})"
    }

    companion object {
        fun createDeck(): MutableList<Card> {
            val deck = mutableListOf<Card>()
            for (suit in Suit.values()) {
                for (rank in Rank.values) {
                    deck.add(Card(suit, rank))
                }
            }
            deck.shuffle()
            return deck
        }
    }
}

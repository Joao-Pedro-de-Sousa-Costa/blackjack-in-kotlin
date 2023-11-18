import src.main.kotlin.Rank
import src.main.kotlin.Suit

class Card(private val suit: Suit, private val rank: Rank) {
    fun getValue() = rank.rankValue
    fun getSuit() = suit
    fun getRank() = rank

     override fun toString(): String {
        return "[$rank of $suit] (${getValue()})"
    }

}

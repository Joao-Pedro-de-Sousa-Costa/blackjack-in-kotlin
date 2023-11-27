import src.main.kotlin.Card
import src.main.kotlin.Rank

class CardCounter {

    private var count: Int = 0

    fun updateCount(card: Card) {
        // Atualiza a contagem com base no valor atribuído à carta
        when (card.getRank()) {
            in Rank.TWO..Rank.SIX, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING -> count++
            Rank.ACE, Rank.SEVEN, Rank.EIGHT, Rank.NINE -> count--
            else -> {}
        }
    }
    fun resetCount() {
        // Reseta a contagem para 0
        count = 0
    }
    fun getCount(): Int {
        return count
    }
}
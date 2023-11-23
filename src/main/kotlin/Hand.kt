package src.main.kotlin

class Hand {
    private val cards = mutableListOf<Card>()

    fun add(card: Card) {
        cards.add(card)
    }

    fun dealCard(deck: MutableList<Card>): Card? {
        if (deck.isNotEmpty()) {
            val lastCard = deck.removeAt(deck.size - 1)
            add(lastCard)
            return lastCard
        } else {
            return null
        }
    }

    fun getScore(): Int { // Verifica se a mão tem um Ás, se houver, ele vai incrementar o num e avaliar, caso o score seja maior que 10, As = 1, numAces--
        var score = 0
        var numAces = 0

        for (card in cards) {
            score += card.getValue()
            if (card.getRank() == Rank.ACE) {
                numAces++
            }
        }

        while (numAces > 0 && score > 21) { // Corrigir depois
            score -= 10
            numAces--
        }

        return score
    }

    fun getCard(idx: Int): Card {
        return cards.get(idx)
    }

    override fun toString(): String {
        return "Hand: $cards, Score: ${getScore()}"
    }
}

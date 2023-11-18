package src.main.kotlin

enum class Suit(val suitName: String) {
    CLUBS("Clubs"),
    DIAMONDS("Diamonds"),
    SPADES("Spades"),
    HEARTS("Hearts");

    override fun toString(): String {
        return suitName
    }

    companion object {
        val values = enumValues<Suit>()
    }
}

package src.main.kotlin

enum class Suit(val suitName: String) {
    CLUBS("Paus"),
    DIAMONDS("Ouro"),
    SPADES("Espadas"),
    HEARTS("Copas");

    override fun toString(): String {
        return suitName
    }

    companion object {
        val values = enumValues<Suit>()
    }
}

package src.main.kotlin

enum class Rank(val rankName: String, val rankValue: Int) {
    ACE("Ace", 11),
    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four", 4),
    FIVE("Five", 5),
    SIX("Six", 6),
    SEVEN("Seven", 7),
    EIGHT("Eight", 8),
    NINE("Nine", 9),
    TEN("Ten", 10),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10);

    override fun toString(): String {
        return rankName
    }

    companion object {
        val values = enumValues<Rank>()
    }
}
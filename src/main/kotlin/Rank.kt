package src.main.kotlin

enum class Rank(val rankName: String, val rankValue: Int) {
    ACE("A", 11),
    TWO("Dois", 2),
    THREE("Três", 3),
    FOUR("Quatro", 4),
    FIVE("Cinco", 5),
    SIX("Seis", 6),
    SEVEN("Sete", 7),
    EIGHT("Oito", 8),
    NINE("Nove", 9),
    TEN("Dez", 10),
    JACK("Valete", 10),
    QUEEN("Rainha", 10),
    KING("Rei", 10);

    override fun toString(): String {
        return rankName
    }

    companion object {
        val values = enumValues<Rank>()
    }
}
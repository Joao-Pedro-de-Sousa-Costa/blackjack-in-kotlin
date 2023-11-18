import src.main.kotlin.Rank
import src.main.kotlin.Suit


fun main(args: Array<String>) {
    println("Bem vindo ao 21")
    println(Suit.CLUBS)
    println(Rank.ACE.rankName+Rank.ACE.rankValue)
    println(Card(Suit.CLUBS, Rank.FOUR))
}
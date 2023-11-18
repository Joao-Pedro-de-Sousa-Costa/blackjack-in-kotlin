import src.main.kotlin.Hand
import src.main.kotlin.Card

fun main(args: Array<String>) {
    val deck = Card.createDeck()
    val playerHand = Hand() // Inicializa a mão do jogador, deve ir pra player


    print("Deck criado: ")
    for (card in deck) {
        print("$card ")
    }

    // Simulando jogador por que ainda não foi feito, retirar depois e adaptar

    val dealtCard = playerHand.dealCard(deck)

    if (dealtCard != null) {
        println("\nCarta recebida: $dealtCard")
        println("Nova mão: $playerHand")
    } else {
        println("\nSem cartas no baralho.")
    }

    print("Deck atual: ")
    for (card in deck) {
        print("$card ")
    }
}

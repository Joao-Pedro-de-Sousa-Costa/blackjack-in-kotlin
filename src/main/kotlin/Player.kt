package src.main.kotlin
import java.util.*

class Player(private var money: Int) {
    private val hand = Hand()
    private var playerName: String = ""
    private var gameDateTime: Date? = null
    private var bet: Int = 0

    fun setName(name: String) {
        playerName = name
    }

    fun getName(): String {
        return playerName
    }

    fun getMoney(): Int {
        return money
    }

    fun getHand(): Hand {
        return hand
    }

    fun setGameDateTime(dateTime: Date) {
        gameDateTime = dateTime
    }

    fun getGameDateTime(): Date? {
        return gameDateTime
    }

    fun placeBet(betAmount: Int): Boolean { // Verificar se pode apostar
        if (betAmount > 0 && betAmount <= money) {
            money -= betAmount
            bet = betAmount // Atualiza a aposta
            return true
        }
        return false
    }

    fun receiveWinnings(winnings: Int) {
        money += winnings
    }

    // Jogadas:

    fun hit(deck: MutableList<Card>) {
        if (hand.getScore() <= 21) {
            val newCard = hand.dealCard(deck)
            println("O jogador ${getName()} solicitou uma carta.")

            if (newCard != null) {
                println("${getName()} recebeu uma carta: $newCard")
            } else {
                println("O baralho acabou. Não é mais possível receber cartas.")
            }
        } else {
            println("${getName()} atingiu ou ultrapassou 21. Não é possível receber mais cartas.")
        }
    }

    fun doubleDown(deck: MutableList<Card>): Int {
        println("O jogador ${getName()} deseja dobrar a aposta.")
        val originalBet = bet

        if (hand.getScore() <= 21 && money >= originalBet * 2) {
            val newCard = hand.dealCard(deck)
            println("${getName()} recebeu uma carta: $newCard")

            money -= originalBet
            bet += originalBet

            return bet
        } else if (hand.getScore() >= 21) {
            println("${getName()} atingiu ou ultrapassou 21. Não é possível receber mais cartas.")
        } else {
            println("${getName()} não tem o valor suficiente para dobrar a aposta.")
        }

        return originalBet
    }

    fun stand() {
        println("O jogador ${getName()} decidiu não pegar mais cartas")
    }

    // fun split(){} - Não sei implementar isso não
    fun surrender() {
        println("O jogador ${getName()} decidiu desistir da aposta.")
    }

    override fun toString(): String {
        return "Player: $playerName, Money: $money, $hand"
    }
}

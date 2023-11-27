package src.main.kotlin

import java.lang.Exception
import java.util.*

open class Player(private var money: Int = 100) {

    var hand = Hand()
    private var playerName: String = ""
    private var bet: Int = 0
    private var standing: Boolean = false

    fun getMoney(): Int {
        return money
    }
    fun setMoney(Pmoney: Int) {
        money = Pmoney
    }
    fun setName(name: String) {
        playerName = name
    }
    fun getName(): String {
        return playerName
    }
    fun getBet(): Int {
        return bet
    }
    fun getStand(): Boolean {
        return standing
    }
    fun placeBet(betAmount: Int): Boolean { // Verificar se pode apostar
        if (betAmount > 0 && betAmount <= money) {
            money -= betAmount
            bet = betAmount // Atualiza a aposta
            return true
        }
        return false
    }

    // Jogadas:

    fun hit(deck: MutableList<Card>, playedCards: MutableList<Card>) {
        if (hand.getScore() <= 21) {
            val newCard = hand.dealCard(deck, playedCards)
            println("\nO jogador ${getName()} solicitou uma carta.")

            if (newCard != null) {
                println("${getName()} recebeu uma carta: $newCard")
                println(this)
            } else {
                println("O baralho acabou. Não é mais possível receber cartas.")
            }
        } else {
            println("${getName()} atingiu ou ultrapassou 21. Não é possível receber mais cartas.")
            standing = true
        }
    }
    fun doubleDown(deck: MutableList<Card>, playedCards: MutableList<Card>): Int {
        println("\nO jogador ${getName()} deseja dobrar a aposta.")
        val originalBet = bet

        if (hand.getScore() <= 21 && money >= originalBet * 2) {
            val newCard = hand.dealCard(deck, playedCards)
            println("${getName()} recebeu uma carta: $newCard")

            money -= originalBet
            bet += originalBet

            return bet
        } else if (hand.getScore() >= 21) {
            println("${getName()} atingiu ou ultrapassou 21. Não é possível receber mais cartas.")
            standing = true
        } else {
            println("${getName()} não tem dinheiro o suficiente para dobrar a aposta.")
        }

        return originalBet
    }
    fun stand(): Boolean {
        println("\nO jogador ${getName()} decidiu não pegar mais cartas")
        standing = true
        return standing

    }

    open fun makeDecision(deck: MutableList<Card>, playedCards: MutableList<Card>) {
        val input = Scanner(System.`in`)
        var decision: Int = 0
        var getNum = true

        while (getNum) {
            try {
                println("\nO que você deseja realizar?")
                println("[1] HIT \n[2] DOUBLE \n[3] STAND")
                decision = input.nextInt()

                if (decision in 1..3) {
                    getNum = false
                } else {
                    println("Opção inválida. Digite 1, 2 ou 3.")
                }
            } catch (e: InputMismatchException) {
                println("Valor inválido, tente novamente.")
                input.next()  // Limpar a entrada incorreta
            }
        }

        when (decision) {
            1 -> {
                this.hit(deck, playedCards)

                if (this.hand.getScore() <= 20) {
                    this.makeDecision(deck, playedCards)
                }
            }
            2 -> {
                this.doubleDown(deck, playedCards)

                if (this.hand.getScore() <= 20) {
                    this.makeDecision(deck, playedCards)
                }
            }
            3 -> this.stand()
        }
    }

    override fun toString(): String {
        return "Player: $playerName, Aposta: $bet, Dinheiro: $money, $hand"
    }
}
package src.main.kotlin

import java.lang.Exception
import java.util.*

open class Player(private var money: Int = 100) {

    var hand = Hand()
    private var playerName: String = ""
    private var gameDateTime: Date? = null
    private var bet: Int = 0
    private var wins: Int = 0
    private var standing: Boolean = false

    fun getMoney(): Int{
        return money
    }
    fun setMoney(Pmoney: Int){
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

    // Jogadas:

    fun hit(deck: MutableList<Card>, playedCards: MutableList<Card>) {
        if (hand.getScore() <= 21) {
            val newCard = hand.dealCard(deck,playedCards)
            println("O jogador ${getName()} solicitou uma carta.")

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
        println("O jogador ${getName()} deseja dobrar a aposta.")
        val originalBet = bet

        if (hand.getScore() <= 21 && money >= originalBet * 2) {
            val newCard = hand.dealCard(deck,playedCards)
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
        println("O jogador ${getName()} decidiu não pegar mais cartas")
        standing = true
        return standing

    }

    open fun makeDecision(deck: MutableList<Card>, playedCards: MutableList<Card>) {
        val input = Scanner(System.`in`)
        var decision: String = ""
        var getNum = true

        while (getNum) {
            try {
                println("Qual a sua opção?")
                println(" --HIT-- \n --DOUBLE-- \n --STAND--")
                decision = input.next().lowercase()
                getNum = false
            } catch (e: Exception) {
                println("Invalido")
                input.next()
            }
        }

        if (decision.equals("hit")) {
            this.hit(deck, playedCards)

            if (this.hand.getScore() > 20) {
                return
            } else {
                this.makeDecision(deck, playedCards)
            }

        } else if (decision.equals("double")) {
            this.doubleDown(deck, playedCards)

            if (this.hand.getScore() > 20) {
                return
            } else {
                makeDecision(deck, playedCards)
            }

        } else {
            this.stand()
        }


    }

    override fun toString(): String {
        return "Player: $playerName, Bet: $bet, Money: $money, $hand"
    }
}

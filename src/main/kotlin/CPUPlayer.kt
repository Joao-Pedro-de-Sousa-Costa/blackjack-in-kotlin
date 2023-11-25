package src.main.kotlin

import java.lang.Exception
import java.util.*

class CPUPlayer : Player() {

    override fun makeDecision(deck: MutableList<Card>) {
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
            this.hit(deck)

            if (this.hand.getScore() > 20) {
                return
            } else {
                this.makeDecision(deck)
            }

        } else if (decision.equals("double")) {
            this.doubleDown(deck)

            if (this.hand.getScore() > 20) {
                return
            } else {
                makeDecision(deck)
            }

        } else {
            this.stand()
        }


    }


}

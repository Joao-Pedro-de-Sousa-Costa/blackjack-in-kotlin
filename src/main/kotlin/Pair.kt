package src.main.kotlin

data class Pair(val player1: Player, val player2: Player) {

    var name: String = player1.getName() + "'s"
    var money: Int = player1.getMoney() + player2.getMoney()
    var doubleBet: Int = 0

    fun getDoubleScore(): Int {
        return player1.hand.getScore() + player1.hand.getScore()
    }
    fun placeBet(betAmount: Int): Boolean { // Verificar se pode apostar
        if (betAmount > 0 && betAmount <= money) {
            money -= betAmount
            doubleBet = betAmount // Atualiza a aposta
            return true
        }
        return false
    }
    override fun toString(): String {
        return "\nJogadores: ${player1.getName()} & ${player2.getName()} - Aposta: ${doubleBet}, Score: ${getDoubleScore()}, Dinheiro: ${money}"
    }
}
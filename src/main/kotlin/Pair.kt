package src.main.kotlin

data class Pair<T, U>(val jogador1: Player, val jogador2: Player) {

    var name: String = jogador1.getName() + "'s"
    var money: Int = jogador1.getMoney() + jogador2.getMoney()
    var doubleBet: Int = 0


    fun getDoubleScore(): Int {
        return jogador1.hand.getScore() + jogador1.hand.getScore()
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
        return "\nJogadores: ${jogador1.getName()} & ${jogador2.getName()} - Aposta: ${doubleBet}, Score: ${getDoubleScore()}, Dinheiro: ${money}"
    }
}
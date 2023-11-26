package src.main.kotlin

data class Pair(val jogador1: Player, val jogador2: Player) {

    var name: String = jogador1.getName() + "s"
    var money: Int = jogador1.getMoney() + jogador2.getMoney()

    fun getDoubleScore(): Int {
        return jogador1.hand.getScore() + jogador1.hand.getScore()
    }
    fun getApostaTotal(): Int {
        return jogador1.getBet() + jogador2.getBet()
    }
    override fun toString(): String {
        return "\nPair(name='$name', money=$money, doubleScore=${getDoubleScore()},\n Jogadores: ${jogador1.getName()} ${jogador2.getName()})"
    }
}
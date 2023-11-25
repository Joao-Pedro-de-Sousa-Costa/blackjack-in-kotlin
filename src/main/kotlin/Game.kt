package src.main.kotlin
import CPUPlayer
import java.util.Scanner

class Game {
    private var player1 = Player()
    //private var machine = CPUPlayer()
    private var deck = Card.createDeck()
    private var playedCards = mutableListOf<Card>()
    private var machines = mutableListOf<CPUPlayer>()
    private val results = mutableListOf<Player>()

    private val scanner = Scanner(System.`in`)

    private var quantMatchs = 1

    init {

        println("Qual o seu nome? ")
        player1.setName(scanner.next())
        menu()

    }

    private fun resetGame() {
        player1.setMoney(Player().getMoney())
        player1.hand = Hand()
        //machine = CPUPlayer()
        machines = mutableListOf()
        deck = Card.createDeck()
        playedCards = mutableListOf()

    }

    private fun resetMatch() {
        player1.hand = Hand()
        for (machine in machines) {
            machine.hand = Hand()
        }
    }

    fun menu() {

        resetGame()

        println("Escolha a quantidade de desafiantes:")
        println("[1] Desafiante. \n[2] Desafiantes. \n [3] Desafiantes. \n [4] Sair.")
        var numberOfOpponents: Int = scanner.nextInt()

        if (numberOfOpponents in setOf(1, 2, 3)) {
            for (i in 1..numberOfOpponents) {
                val newMachine = CPUPlayer()  // Crie uma nova instância aqui
                newMachine.setName("Oponente$i")
                machines.add(newMachine)
            }

        } else if (numberOfOpponents == 4) {
            return
        } else {
            println("Comando invalido")
            menu()
        }



        println("Escolha seu modo de jogo:")
        println("[1] Modo Normal. \n[2] Modo duplas. \n [3] Sair.")
        val choice = scanner.nextInt()

        if (choice == 1) {
            modeNormalGame()

        } else if (choice == 2) {
            modeDoublegame()

        } else if (choice == 3) {
            return

        } else {
            println("Comando invalido.")
            menu()
        }

    }


    private fun modeNormalGame() {

        println(machines)

        var choice = "s"
        println("Gerando arquivo do jogo...")//Fazer os resultados

        while (choice == "s" && deck.size >= (1 + machines.size) * 2) {
            println("Iniando Partida...")
            match()
            quantMatchs++
            print("Deseja continuar jogando?(S/N)")
            choice = scanner.next().lowercase()
            if(choice == "s") resetMatch()
        }


        val sortedResults = results.sortedByDescending { it.getMoney() }

        // Exibir o pódio
        println("Podium:")
        for ((index, player) in sortedResults.withIndex()) {
            val playerName = player.getName()
            val money = player.getMoney()
            println("${index + 1}. $playerName - Dinheiro: $money")
        }

    }

    private fun modeDoublegame(){

    }

    private fun match() {

        for (player in listOf(player1) + machines) {
            results.add(player)
        }

        var quantPlayers = 1 + machines.size
        if (deck.size >= quantPlayers * 2) {

            println("Partida $quantMatchs")
            val scanner = Scanner(System.`in`)
            println("Quanto voce quer apostar?")
            player1.placeBet(scanner.nextInt())


            machines.forEach { machine ->
                println("Antes: $machine")
                machine.placeBet(player1.getBet())
                println("Depois: $machine")
            }
            var allPlayer = (listOf(player1) + machines)
            var generalBet = allPlayer.sumOf { it.getBet() }
            println(generalBet)

            for (player in listOf(player1) + machines) {
                player.hand.dealCard(deck,playedCards)
                player.hand.dealCard(deck,playedCards)
                println(player)
            }

            for (player in listOf(player1) + machines) {
                println("Turno do jogador ${player.getName()}")

                if (player.hand.getScore() == 21) {
                    println("A partida acabou(MATCH), o vencedor é o $player, ele recebeu +$generalBet dinheiros(mais 20% por ser um blackjack)")
                    player.setMoney((player.getMoney() + generalBet))
                    println(player)
                    return
                }
                player.makeDecision(deck,playedCards)
                generalBet = allPlayer.sumOf { it.getBet() }

                if (player.hand.getScore() == 21) {
                    println("A partida(MATCH) acabou, o vencedor é o $player, ele recebeu +$generalBet dinheiros(mais 20% por ser um blackjack)")
                    player.setMoney((player.getMoney() + generalBet))
                    println(player)

                    return

                }
            }

            val winner = findWinner(listOf(player1) + machines)
            if (winner != null) {
                println(generalBet)
                println(winner)
                println(
                    "A partida(MATCH) acabou, o vencedor é o ${winner.getName()} com escore ${
                        winner.hand.getScore()
                    }, ele recebeu +$generalBet dinheiros"
                )
                winner.setMoney(winner.getMoney() + generalBet)
                println(generalBet)
                println(winner)
            } else {
                println("A partida(MATCH) acabou, nenhum vencedor encontrado.")
            }


        } else {
            println("A quantidade de cartas no deck é insuficiente pra uma partida, FIM DE JOGO.")
            println("Salvando resultados...")//Fazer o metodo que salva os resultados
            println("Deseja continuar jogando? (S/N)")
            var choice = scanner.next().lowercase()
            if (choice == "s") {
                menu()
            } else {
                return
            }

        }
    }

    fun findWinner(jogadores: List<Player>): Player? {
        return jogadores
            .filter { it.hand.getScore() < 21 }
            .maxByOrNull { it.hand.getScore() }

    }
}

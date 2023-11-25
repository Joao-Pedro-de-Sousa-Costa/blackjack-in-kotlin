package src.main.kotlin

import java.util.Scanner

class Game {
    private var player1 = Player()
    private var machine = CPUPlayer()
    private var deck = Card.createDeck()
    private var machines = mutableListOf<CPUPlayer>()

    val scanner = Scanner(System.`in`)

    private var quantMatchs = 1

    init {

        //println("Qual o seu nome? ")
        //player1.setName(scanner.next())
        //match()
        //println(player1)
        //player1.setMoney(2)
        //println(player1)
        //resetGame()
        //println(player1)


    }

    private fun resetGame() {
        player1.setMoney(Player().getMoney())
        machine = CPUPlayer()
        machines = mutableListOf()
        deck = Card.createDeck()
    }
    
    private fun resetMatch(){
        
    }

    fun menu() {

        resetGame()

        println("Escolha a quantidade de desafiantes:")
        println("[1] Desafiante. \n[2] Desafiantes. \n [3] Desafiantes. \n [4] Sair.")
        var numberOfOpponents: Int = scanner.nextInt()

        if (numberOfOpponents in setOf(1, 2, 3)) {

            for (i in 1..numberOfOpponents) {


                machine.setName("Oponente$i")
                machines.add(machine)
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
            //    modeNormalGame()

        } else if (choice == 2) {
            //modeDoublegame()

        } else if (choice == 3) {
            return

        } else {
            println("Comando invalido.")
            menu()
        }

    }


    private fun modeNormalGame() {
        var choice = "s"
        while (choice.equals("s") || deck.size >= (1 + machines.size) * 2) {
            println("Iniando Partida...")
            match()
            print("Deseja continuar jogando?(S/N)")
        }


    }

    private fun match() {
        var quantPlayers = 1 + machines.size
        if (deck.size >= quantPlayers * 2) {

            println("Partida $quantMatchs")
            val scanner = Scanner(System.`in`)
            println("Quanto voce quer apostar?")
            player1.placeBet(scanner.nextInt())
            var generalBet = (listOf(player1) + machines).sumOf { it.getBet() }

            machines.forEach { machine ->
                machine.placeBet(player1.getBet())
            }

            for (player in listOf(player1) + machines) {
                player.getHand().dealCard(deck)
                player.getHand().dealCard(deck)
                println(player)
            }

            for (player in listOf(player1) + machines) {
                println("Turno do jogador ${player.getName()}")

                if (player.getHand().getScore() == 21) {
                    println("A partida acabou(MATCH), o vencedor é o $player, ele recebeu +$generalBet dinheiros")
                    player.setMoney(player.getMoney() + generalBet)
                    return
                }
                player.makeDecision(deck)

                if (player.getHand().getScore() == 21) {
                    println("A partida(MATCH) acabou, o vencedor é o $player, ele recebeu +$generalBet dinheiros")
                    player.setMoney(player.getMoney() + generalBet)

                    return

                }
            }

            val winner = findWinner(listOf(player1) + machines)
            if (winner != null) {
                println("A partida(MATCH) acabou, o vencedor é o ${winner.getName()} com escore ${winner.getHand().getScore()}, ele recebeu +$generalBet dinheiros")
                winner.setMoney(winner.getMoney() + generalBet)
            } else {
                println("A partida(MATCH) acabou, nenhum vencedor encontrado.")
            }


        } else {
            println("A quantidade de cartas no deck é insuficiente pra uma partida, FIM DE JOGO.")
            println("Salvando resultados...")//Fazer o metodo que salva os resultados
            println("Deseja continuar jogando? (S/N)")
            var choice = scanner.next().lowercase()
            if (choice.equals("s")) {
                menu()
            } else {
                return
            }

        }
    }

    fun findWinner(jogadores: List<Player>): Player? {
        return jogadores
            .filter { it.getHand().getScore() < 21 }
            .maxByOrNull { it.getHand().getScore() } 

    }
}

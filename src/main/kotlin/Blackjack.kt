package src.main.kotlin
import CPUPlayer
import java.util.Scanner

class Blackjack {
    private val scanner = Scanner(System.`in`)

    private var human = Player()
    private var machines = mutableListOf<CPUPlayer>()
    private val gameResults = mutableListOf<Player>()

    private val pair = mutableListOf<Pair>()
    private val pairGameResults = mutableListOf<Pair>()

    private var deck = Card.createDeck()
    private var playedCards = mutableListOf<Card>()

    private var quantMatchs = 1

    init {
        var name = ""
        do {
            print("Insira o seu nome: ")
            name = readLine() ?: ""
            if (name.isBlank()) {
                println("Nome inválido, tente novamente.")
            }
        } while (name.isBlank())

        human.setName(name)
        menu()
    }

    private fun resetGame() {
        human.setMoney(Player().getMoney())
        human.hand = Hand()
        machines = mutableListOf()
        deck = Card.createDeck()
        playedCards = mutableListOf()
    }

    private fun resetMatch() {
        human.hand = Hand()
        for (machine in machines) {
            machine.hand = Hand()
        }
    }

    fun menu() {
        resetGame()
        var isNormalGame = false
        var isPairGame = false

        println("\nEscolha seu modo de jogo:")
        println("[1] Padrão \n[2] Duplas \n[3] Sair")

        try {
            when (val choice = scanner.nextInt()) {
                1 -> isNormalGame = true
                2 -> isPairGame = true
                3 -> return
                else -> {
                    println("Comando inválido, insira novamente.")
                    menu()
                    return
                }
            }
        } catch (e: java.util.InputMismatchException) {
            println("Entrada inválida. Por favor, insira um número.")
            scanner.next() // Limpar a entrada incorreta
            menu()
            return
        }

        if (isNormalGame) {
            while (true) {
                println("\nInsira a quantidade de desafiantes: ")
                println("[1] 1v1 \n[2] 1v2 \n[3] 1v3 \n[4] Sair")

                try {
                    when (val opponentsChoice = scanner.nextInt()) {
                        in setOf(1, 2, 3) -> {
                            for (i in 1..opponentsChoice) {
                                val newMachine = CPUPlayer()
                                newMachine.setName("Oponente$i")
                                machines.add(newMachine)
                            }
                            break
                        }

                        4 -> return
                        else -> {
                            println("Comando inválido, tente novamente.")
                        }
                    }
                } catch (e: java.util.InputMismatchException) {
                    println("Entrada inválida. Por favor, insira um número.")
                    scanner.next()
                }
            }
            modeNormalGame()
        }

        if (isPairGame) {
            while (true) {
                println("\nEscolha a quantidade de duplas:")
                println("[1] 2v2 \n[2] 2v4 \n[3] 2v6 \n[4] Sair")

                try {
                    when (val option: Int = scanner.nextInt()) {
                        1 -> createPairs(2)
                        2 -> createPairs(4)
                        3 -> createPairs(6)
                        4 -> return
                        else -> {
                            println("Opção inválida, tente novamente.")
                            continue
                        }
                    }
                    modePairGame()
                    break
                } catch (e: java.util.InputMismatchException) {
                    println("Entrada inválida. Por favor, insira um número.")
                    scanner.next()
                }
            }
        }
    }

    private fun createPairs(numPairs: Int) {
        pair.clear()
        machines.clear()

        val firstPair = Pair(human, CPUPlayer())
        machines.add(firstPair.jogador2 as CPUPlayer)
        pair.add(firstPair)

        for (i in 1..numPairs/2) {
            val newMachine1 = CPUPlayer()
            newMachine1.setName("Máquina$i-A")
            machines.add(newMachine1)

            val newMachine2 = CPUPlayer()
            newMachine2.setName("Máquina$i-B")
            machines.add(newMachine2)

            val newPair = Pair(newMachine1, newMachine2)
            pair.add(newPair)
        }
    }

    private fun modeNormalGame() {
        while (true) {
            match()
            ++quantMatchs

            try {
                print("Deseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                val option: String = scanner.next().lowercase()

                when (option) {
                    "s" -> {
                        menu()
                        break
                    }
                    "n" -> return
                    else -> {
                        println("Opção inválida, tente novamente.")
                    }
                }
            } catch (e: java.util.InputMismatchException) {
                println("Entrada inválida. Por favor, insira uma opção válida.")
                scanner.next()
            }
        }

        val sortedResults = gameResults.sortedByDescending { it.getMoney() }

        println("Podium:")
        sortedResults.forEachIndexed { index, player ->
            val playerName = player.getName() ?: "Desconhecido"
            val money = player.getMoney()
            println("${index + 1}. $playerName - Dinheiro: $money")
        }
    }

    private fun modePairGame() {
        while (true) {
            println("Iniciando Partida...")
            matchInDouble()
            ++quantMatchs

            try {
                print("Deseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                val option: String = scanner.next().lowercase()

                when (option) {
                    "s" -> {
                        menu()
                        break
                    }
                    "n" -> return
                    else -> {
                        println("Opção inválida, tente novamente.")
                    }
                }
            } catch (e: java.util.InputMismatchException) {
                println("Entrada inválida. Por favor, insira uma opção válida.")
                scanner.next()
            }
        }

        val sortedResults = gameResults.sortedByDescending { it.getMoney() }

        println("Podium:")
        sortedResults.forEachIndexed { index, player ->
            val playerName = player.getName() ?: "Desconhecido"
            val money = player.getMoney()
            println("${index + 1}. $playerName - Dinheiro: $money")
        }
    }

    private fun match() {
        val allPlayers = listOf(human) + machines

        for (player in allPlayers) {
            gameResults.add(player)
        }

        val numPlayers = 1 + machines.size
        if (deck.size < numPlayers * 2) {
            println("A quantidade de cartas no deck é insuficiente para uma partida.\nFIM DE JOGO.")
            println("Salvando resultados...") // Fazer o método que salva os resultados

            while (true) {
                try {
                    print("Deseja continuar jogando? \n" +
                            "[S] Continuar\n" +
                            "[N] Sair")
                    val option: String = scanner.next().lowercase()

                    when (option) {
                        "s" -> {
                            menu()
                            break
                        }
                        "n" -> return
                        else -> {
                            println("Opção inválida, tente novamente.")
                        }
                    }
                } catch (e: java.util.InputMismatchException) {
                    println("Entrada inválida. Por favor, insira uma opção válida.")
                    scanner.next()
                }
            }
        }

        println("\nPartida $quantMatchs")
        val scanner = Scanner(System.`in`)

        print("\nInsira o valor que deseja apostar: ")
        human.placeBet(scanner.nextInt())

        machines.forEach { machine ->
            machine.placeBet(human.getBet())
        }

        var generalBet = allPlayers.sumOf { it.getBet() }
        println("Valor total da mesa: $generalBet")

        println("\nOPONENTES EM MESA\n")

        for (player in allPlayers) {


            player.hand.dealCard(deck, playedCards)
            player.hand.dealCard(deck, playedCards)
            println(player)
        }

        for (player in allPlayers) {
            println("\nTurno de: ${player.getName()}")

            if (player.hand.getScore() == 21) {
                println("A partida acabou, o vencedor é o $player.\nValor recebido: $generalBet (+20% por ser um Blackjack)")
                player.setMoney(player.getMoney() + generalBet)
                return
            }

            player.makeDecision(deck, playedCards)
            generalBet = allPlayers.sumOf { it.getBet() }

            if (player.hand.getScore() == 21) {
                println("A partida acabou, o vencedor é o $player.\nValor recebido: $generalBet (+20% por ser um Blackjack)")
                player.setMoney(player.getMoney() + generalBet)
                return
            }
        }

        val winner = findWinner(allPlayers)
        if (winner != null) {
            println(generalBet)
            println(winner)
            println("A partida acabou, o vencedor é o ${winner.getName()} com score ${winner.hand.getScore()}.\nValor recebido: $generalBet")
            winner.setMoney(winner.getMoney() + generalBet)
        } else {
            println("A partida acabou, nenhum vencedor encontrado.")
        }
    }

    private fun matchInDouble() {
        for (dupla in pair) {
            gameResults.add(dupla.jogador1)
            gameResults.add(dupla.jogador2)
        }

        val quantPlayers = 1 + machines.size
        if (deck.size < quantPlayers * 2) {
            println("A quantidade de cartas no deck é insuficiente para uma partida.\n FIM DE JOGO.")
            println("Salvando resultados...")

            while (true) {
                try {
                    print("Deseja continuar jogando? \n" +
                            "[S] Continuar\n" +
                            "[N] Sair")
                    val option: String = scanner.next().lowercase()

                    when (option) {
                        "s" -> {
                            menu()
                            break
                        }
                        "n" -> return
                        else -> {
                            println("Opção inválida, tente novamente.")
                        }
                    }
                } catch (e: java.util.InputMismatchException) {
                    println("Entrada inválida. Por favor, insira uma opção válida.")
                    scanner.next()
                }
            }
        }

        println("Partida $quantMatchs")
        val scanner = Scanner(System.`in`)

        print("Insira o valor que deseja apostar: ")
        val apostaJogador1 = scanner.nextInt()

        for (dupla in pair) {
            dupla.jogador1.placeBet(apostaJogador1)
            dupla.jogador2.placeBet(apostaJogador1)
        }

        var generalBet = pair.sumOf { it.getApostaTotal() }
        println("Aposta total: $generalBet")

        for (dupla in pair) {
            dupla.jogador1.hand.dealCard(deck, playedCards)
            dupla.jogador1.hand.dealCard(deck, playedCards)
            dupla.jogador2.hand.dealCard(deck, playedCards)
            dupla.jogador2.hand.dealCard(deck, playedCards)
        }

        for (dupla in pair) {
            println("Turno da dupla: ${dupla.name}")

            for (jogador in listOf(dupla.jogador1, dupla.jogador2)) {
                println("Turno do jogador ${jogador.getName()}")

                if (dupla.getDoubleScore() == 42) {
                    println("A partida acabou, o vencedor é o $dupla.\nValor recebido: $generalBet (+20% por ser um Blackjack)")
                    dupla.money += generalBet
                    println(dupla)
                    return
                }

                jogador.makeDecision(deck, playedCards)
                generalBet = pair.sumOf { it.getApostaTotal() }

                if (dupla.getDoubleScore() == 42) {
                    println("A partida acabou, o vencedor é o $dupla.\nValor recebido: $generalBet (+20% por ser um Double Blackjack)")
                    dupla.money += generalBet
                    println(dupla)
                    return
                }
            }
        }

        val winners = findWinningDouble(pair)
        if (winners != null) {
            println(generalBet)
            println(winners)
            println(
                "A partida acabou, o vencedor é o ${winners.name} com score ${winners.getDoubleScore()}\nValor recebido: $generalBet"
            )
            winners.money += generalBet
            println(generalBet)
            println(winners)
        } else {
            println("A partida acabou, nenhum vencedor encontrado.")
        }
    }

    fun findWinner(jogadores: List<Player>): Player? {
        return jogadores
                .filter { it.hand.getScore() < 21 }
                .maxByOrNull { it.hand.getScore() }

    }

    fun findWinningDouble(duplas: List<Pair>): Pair? {
        return duplas
                .filter { it.getDoubleScore() < 42 }
                .maxByOrNull { it.getDoubleScore() }
    }
}

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

        println("Escolha seu modo de jogo:")
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
                println("Insira a quantidade de desafiantes: ")
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
                println("Escolha a quantidade de duplas:")
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
                    modeDoublegame()
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
        println(machines)

        var choice: String
        while (true) {
            println("Iniciando Partida...")
            match()
            ++quantMatchs

            try {
                print("Deseja continuar jogando? (S/N)")
                choice = scanner.next().lowercase()
                if (choice != "s") break
                resetMatch()
            } catch (e: java.util.InputMismatchException) {
                println("Entrada inválida. Por favor, insira uma opção válida.")
                scanner.next() // Limpar a entrada incorreta
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

    private fun modeDoublegame() {

        var choice = "s"
        println("Gerando arquivo do jogo...")//Fazer os resultados
        println("Quantidade de inimigos selecionados:$pair");
        println(machines)

        while (choice == "s" && deck.size >= (1 + machines.size) * 2) {
            println("Iniando Partida...")
            matchInDouble()
            quantMatchs++
            print("Deseja continuar jogando?(S/N)")
            choice = scanner.next().lowercase()
            if (choice == "s") resetMatch()
        }


        val sortedResults = pairGameResults.sortedByDescending { it.getApostaTotal() }

// Exibir o pódio
        println("Podium:")
        for ((index, dupla) in sortedResults.withIndex()) {
            val duplaNome = dupla.name
            val jogador1Nome = dupla.jogador1.getName()
            val jogador2Nome = dupla.jogador2.getName()
            val dinheiro = dupla.getApostaTotal()
            println("${index + 1}. $duplaNome - Jogadores: $jogador1Nome, $jogador2Nome - Dinheiro: $dinheiro")
        }

    }

    private fun match() {


        for (player in listOf(human) + machines) {
            gameResults.add(player)
        }

        var quantPlayers = 1 + machines.size
        if (deck.size >= quantPlayers * 2) {

            println("Partida $quantMatchs")
            val scanner = Scanner(System.`in`)
            println("Quanto voce quer apostar?")
            human.placeBet(scanner.nextInt())


            machines.forEach { machine ->
                println("Antes: $machine")
                machine.placeBet(human.getBet())
                println("Depois: $machine")
            }
            var allPlayer = (listOf(human) + machines)
            var generalBet = allPlayer.sumOf { it.getBet() }
            println(generalBet)

            for (player in listOf(human) + machines) {
                player.hand.dealCard(deck, playedCards)
                player.hand.dealCard(deck, playedCards)
                println(player)
            }

            for (player in listOf(human) + machines) {
                println("Turno do jogador ${player.getName()}")

                if (player.hand.getScore() == 21) {
                    println("A partida acabou(MATCH), o vencedor é o $player, ele recebeu +$generalBet dinheiros(mais 20% por ser um blackjack)")
                    player.setMoney((player.getMoney() + generalBet))
                    println(player)
                    return
                }
                player.makeDecision(deck, playedCards)
                generalBet = allPlayer.sumOf { it.getBet() }

                if (player.hand.getScore() == 21) {
                    println("A partida(MATCH) acabou, o vencedor é o $player, ele recebeu +$generalBet dinheiros(mais 20% por ser um blackjack)")
                    player.setMoney((player.getMoney() + generalBet))
                    println(player)

                    return

                }
            }

            val winner = findWinner(listOf(human) + machines)
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

    private fun matchInDouble() {
        for (dupla in pair) {
            gameResults.add(dupla.jogador1)
            gameResults.add(dupla.jogador2)
        }

        var quantPlayers = 1 + machines.size
        if (deck.size >= quantPlayers * 2) {

            println("Partida $quantMatchs")
            val scanner = Scanner(System.`in`)

            println("Quanto você quer apostar?")
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

                // Turnos alternados entre os jogadores da dupla
                for (jogador in listOf(dupla.jogador1, dupla.jogador2)) {
                    println("Turno do jogador ${jogador.getName()}")

                    // Lógica para o turno do jogador
                    if (dupla.getDoubleScore() == 42) {
                        println("A partida acabou(MATCH), o vencedor é o $dupla, eles receberam +$generalBet dinheiros(mais 20% por ser um double blackjack)")
                        dupla.money += generalBet
                        println(dupla)
                        return
                    }

                    jogador.makeDecision(deck, playedCards)
                    generalBet = pair.sumOf { it.getApostaTotal() }

                    if (dupla.getDoubleScore() == 42) {
                        println("A partida acabou(MATCH), o vencedor é $dupla, eles receberam +$generalBet dinheiros(mais 20% por ser um double blackjack)")
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
                        "A partida(MATCH) acabou, o vencedor é o ${winners.name} com escore ${
                            winners.getDoubleScore()
                        }, eles receberam +$generalBet dinheiros"
                )
                winners.money += generalBet
                println(generalBet)
                println(winners)
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

    fun findWinningDouble(duplas: List<Pair>): Pair? {
        return duplas
                .filter { it.getDoubleScore() < 42 }
                .maxByOrNull { it.getDoubleScore() }
    }
}

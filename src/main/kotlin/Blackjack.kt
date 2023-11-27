package src.main.kotlin

import CPUPlayer
import java.util.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Blackjack {
    private val scanner = Scanner(System.`in`)

    // Jogadores
    private var human = Player()
    private var machines = mutableListOf<CPUPlayer>()
    private val gameResults = mutableListOf<Player>()

    // Pares (se estiver usando)
    private val pair = mutableListOf<Pair<Any?, Any?>>()
    private val pairGameResults = mutableListOf<Pair<Any?, Any?>>()

    // Baralho
    private var deck = Card.createDeck()
    private var playedCards = mutableListOf<Card>()

    // Contadores de partidas e jogos
    private var quantMatchs = 1
    private var quantGames = carregarQuantidadeDeJogos()

    // Escritor para resultados
    val escritor = BufferedWriter(FileWriter("Resultados.txt", true))

    // Formato de hora e data
    val formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        escritor.newLine()


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
        escritor.close()

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
                                newMachine.setName("Maquina-$i")
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

        val firstPair = Pair<Any?,Any?>(human, CPUPlayer())
        machines.add(firstPair.jogador2 as CPUPlayer)
        firstPair.jogador2.setName("Maquina Aux")
        pair.add(firstPair)

        for (i in 1..numPairs / 2) {
            val newMachine1 = CPUPlayer()
            newMachine1.setName("Máquina$i A")
            machines.add(newMachine1)

            val newMachine2 = CPUPlayer()
            newMachine2.setName("Máquina$i B")
            machines.add(newMachine2)

            val newPair = Pair<Any?,Any?>(newMachine1, newMachine2)
            pair.add(newPair)
        }
    }
    private fun modeNormalGame() {
        quantGames++
        salvarQuantidadeDeJogos()
        escritor.newLine()
        escritor.append("${quantGames}. ---JOGO--- .${quantGames}")
        escritor.newLine()
        escritor.append("Hora atual: ${LocalTime.now().format(formatoHora)}; Data Atual: ${LocalDate.now().format(formatoData)}\n")
        escritor.newLine()
        while (deck.size >= (1 + machines.size) * 2) {
            var option = ""
            println(" ")
            match()
            ++quantMatchs

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> resetMatch()
                "n" -> return printResults(listOf(human)+machines)
            }
        }
    }
    private fun modePairGame() {
        quantGames++
        salvarQuantidadeDeJogos()
        escritor.newLine()
        escritor.append("${quantGames}. ---JOGO--- .${quantGames}")
        escritor.newLine()
        escritor.append("Hora atual: ${LocalTime.now().format(formatoHora)}; Data Atual: ${LocalDate.now().format(formatoData)}\n")
        escritor.newLine()

        while (deck.size >= (1 + machines.size) * 2) {
            var option = ""
            println(" ")
            matchInDouble()
            ++quantMatchs

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> resetMatch()
                "n" -> return
            }
        }
    }
    private fun match() {
        val allPlayers = listOf(human) + machines

        val numPlayers = 1 + machines.size
        if (deck.size < numPlayers * 2) {
            println("A quantidade de cartas no deck é insuficiente para uma partida.\nFIM DE JOGO.")
            println("Salvando resultados...") // Fazer o método que salva os resultados

            var option = ""

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> menu()
                "n" -> return printResults(allPlayers)
            }
        }

        println("\nPartida $quantMatchs")
        val scanner = Scanner(System.`in`)

        try {
            print("\nInsira o valor que deseja apostar: ")
            var valor = scanner.nextInt()

            if (human.getMoney() != 0 && human.getMoney() >= valor) {
                human.placeBet(valor)

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
                    println("${player.hand}")
                    if (player.hand.getScore() == 21) {
                        println("\nA partida acabou, o vencedor é o $player.\nValor recebido: $generalBet (+10 por ser um Blackjack)")
                        player.setMoney((player.getMoney() + generalBet) + 10)
                        return
                    }

                    player.makeDecision(deck, playedCards)
                    generalBet = allPlayers.sumOf { it.getBet() }

                    if (player.hand.getScore() == 21) {
                        println("\nA partida acabou, o vencedor é o $player.\nValor recebido: $generalBet (+10 por ser um Blackjack)")
                        player.setMoney((player.getMoney() + generalBet) + 10)
                        return
                    }
                }

                val winner = findWinner(allPlayers)
                if (winner != null) {
                    //println(generalBet)
                    //println(winner)
                    println("\nA partida acabou, o vencedor é o ${winner.getName()} com score ${winner.hand.getScore()}.\nValor recebido: $generalBet")
                    winner.setMoney(winner.getMoney() + generalBet)
                } else {
                    println("\nA partida acabou, nenhum vencedor encontrado.")
                }
            } else {
                println("Aposta inválida. Certifique-se de que você tem dinheiro suficiente.")
                quantMatchs--
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Insira um valor numérico.")
            quantMatchs--
            scanner.next() // Limpar o buffer do scanner
        }
    }
    private fun matchInDouble() {
        println("Duplas: \n $pair")

        val numPlayers = 1 + machines.size
        if (deck.size < numPlayers * 2) {
            println("A quantidade de cartas no deck é insuficiente para uma partida.\nFIM DE JOGO.")
            println("Salvando resultados...") // Fazer o método que salva os resultados

            var option = ""

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> menu()
                "n" -> return printResults(pair)
            }
        }
        println("\nPartida $quantMatchs")
        val scanner = Scanner(System.`in`)
        try {
            print("\nInsira o valor que deseja apostar: ")
            var valor = scanner.nextInt()

            if (human.getMoney() != 0 && human.getMoney() >= valor) {
                human.placeBet(valor)

                for (dupla in pair) {
                    dupla.placeBet(valor)
                }

                var generalBet = pair.sumOf { it.doubleBet }
                println("Valor total da mesa: $generalBet")

                println("\nOPONENTES EM MESA\n")

                for (dupla in pair) {
                    dupla.jogador1.hand.dealCard(deck, playedCards)
                    dupla.jogador1.hand.dealCard(deck, playedCards)
                    dupla.jogador2.hand.dealCard(deck, playedCards)
                    dupla.jogador2.hand.dealCard(deck, playedCards)

                }
                println(pair)

                for (dupla in pair) {
                    println("\nTurno da dupla: ${dupla.name}")

                    for (jogador in listOf(dupla.jogador1, dupla.jogador2)) {
                        println("\nTurno de ${jogador.getName()}")
                        println("Mão atual: ${jogador.hand}")

                        if (dupla.getDoubleScore() == 42) {
                            println("\nA partida acabou, o vencedor é o $dupla.\nValor recebido: $generalBet (+20 por ser um Double Blackjack)")
                            dupla.money += (generalBet + 20)
                            println(dupla)
                            return
                        }

                        jogador.makeDecision(deck, playedCards)
                        generalBet = pair.sumOf { it.doubleBet }

                        if (dupla.getDoubleScore() == 42) {
                            println("\nA partida acabou, o vencedor é o $dupla.\nValor recebido: $generalBet (+20 por ser um Double Blackjack)")
                            dupla.money += (generalBet + 20)
                            println(dupla)
                            return
                        }
                    }
                }

                val winners = findWinningDouble(pair)
                if (winners != null) {
                    //println(generalBet)
                    //println(winners)
                    println(
                            "\nA partida acabou, o vencedor é o ${winners.name} com score ${winners.getDoubleScore()}\nValor recebido: $generalBet"
                    )
                    winners.money += generalBet
                    //println(generalBet)
                    //println(winners)
                } else {
                    println("\nA partida acabou, nenhum vencedor encontrado.")
                }
            } else {
                println("Aposta inválida. Certifique-se de que você tem dinheiro suficiente.")
                quantMatchs--
            }
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Insira um valor numérico.")
            quantMatchs--
            scanner.next() // Limpar o buffer do scanner
        }
    }
    fun findWinner(jogadores: List<Player>): Player? {
        return jogadores
                .filter { it.hand.getScore() < 21 }
                .maxByOrNull { it.hand.getScore() }

    }
    fun findWinningDouble(duplas: List<Pair<Any?, Any?>>): Pair<Any?, Any?>? {
        return duplas
                .filter {
                    it.getDoubleScore() < 42 &&
                            it.jogador1.hand.getScore() < 21 &&
                            it.jogador2.hand.getScore() < 21
                }
                .maxByOrNull { it.getDoubleScore() }
    }

    fun printResults(users: List<Any>) {
        val sortedResults = users.sortedByDescending {
            when (it) {
                is Player -> it.getMoney()
                is Pair<*, *> -> (it.jogador1 as Player).getMoney() + (it.jogador2 as Player).getMoney()
                else -> throw IllegalArgumentException("Tipo de usuário não suportado")
            }
        }

        println("Podium:")
        escritor.append("Podium:")
        sortedResults.forEachIndexed { index, user ->
            val playerName = when (user) {
                is Player -> user.getName() ?: "Desconhecido"
                is Pair<*, *> -> (user.jogador1 as Player).getName() + " e " + (user.jogador2 as Player).getName()
                else -> throw IllegalArgumentException("Tipo de usuário não suportado")
            }
            val money = when (user) {
                is Player -> user.getMoney()
                is Pair<*, *> -> (user.jogador1 as Player).getMoney() + (user.jogador2 as Player).getMoney()
                else -> throw IllegalArgumentException("Tipo de usuário não suportado")
            }

            println("${index + 1}. $playerName - Dinheiro: $money")
            escritor.newLine()
            escritor.append("${index + 1}. $playerName - Dinheiro: $money")
        }

        println("Resultados Salvos com sucesso!")
    }
    fun salvarQuantidadeDeJogos() {
        val arquivo = File("config.txt")
        arquivo.writeText(quantGames.toString())
    }
    fun carregarQuantidadeDeJogos(): Int {
        val arquivo = File("config.txt")
        return if (arquivo.exists()) {
            arquivo.readText().toIntOrNull() ?: 0
        } else {
            0
        }
    }
}
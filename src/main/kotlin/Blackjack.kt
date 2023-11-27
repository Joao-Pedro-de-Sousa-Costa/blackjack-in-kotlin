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

    // Duplas (se estiver usando)
    private var pairs = mutableListOf<Pair>()

    // Baralho
    private var deck = Card.createDeck()
    private var playedCards = mutableListOf<Card>()

    // Contadores de partidas e jogos
    private var quantMatchs = 1
    private var quantGames = loadGames()

    // Escritor para resultados
    val writer = BufferedWriter(FileWriter("Resultados.txt", true))

    // Formato de hora e data
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        writer.newLine()

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
        writer.close()
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
        } catch (e: InputMismatchException) {
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
                } catch (e: InputMismatchException) {
                    println("Entrada inválida. Por favor, insira um número.")
                    scanner.next()
                }
            }
            normalMode()
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
                    pairMode()
                    break
                } catch (e: InputMismatchException) {
                    println("Entrada inválida. Por favor, insira um número.")
                    scanner.next()
                }
            }
        }
    }

    private fun normalMode() {
        quantGames++
        saveGames()
        writer.newLine()
        writer.append("${quantGames}. ---JOGO--- .${quantGames}")
        writer.newLine()
        writer.append("Hora atual: ${LocalTime.now().format(timeFormatter)}; Data Atual: ${LocalDate.now().format(dateFormatter)}\n")
        writer.newLine()
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
                "n" -> return printNormalResults()
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
                "n" -> return printNormalResults()
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

    fun findWinner(players: List<Player>): Player? {
        return players
                .filter { it.hand.getScore() < 21 }
                .maxByOrNull { it.hand.getScore() }

    }

    private fun pairMode() {
        quantGames++
        saveGames()
        writer.newLine()
        writer.append("${quantGames}. ---JOGO--- .${quantGames}")
        writer.newLine()
        writer.append("Hora atual: ${LocalTime.now().format(timeFormatter)}; Data Atual: ${LocalDate.now().format(dateFormatter)}\n")
        writer.newLine()

        while (deck.size >= (1 + machines.size) * 2) {
            var option = ""
            println(" ")
            pairMatch()
            ++quantMatchs

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> resetMatch()
                "n" -> return printPodium(pairs)
            }
        }
    }

    private fun createPairs(numPairs: Int) {

        val firstPair = Pair(human, CPUPlayer())
        machines.add(firstPair.player2 as CPUPlayer)
        firstPair.player2.setName("Maquina Aux")
        pairs.add(firstPair)

        for (i in 1..numPairs / 2) {
            val newMachine1 = CPUPlayer()
            newMachine1.setName("Máquina$i A")
            machines.add(newMachine1)

            val newMachine2 = CPUPlayer()
            newMachine2.setName("Máquina$i B")
            machines.add(newMachine2)

            val newPair = Pair(newMachine1, newMachine2)
            pairs.add(newPair)
        }
    }

    private fun pairMatch() {
        println("Duplas: \n $pairs")

        val numPlayers = 1 + machines.size
        if (deck.size < numPlayers * 2) {
            println("A quantidade de cartas no deck é insuficiente para uma partida.\nFIM DE JOGO.")
            println("Salvando resultados...")

            var option = ""

            while (option != "s" && option != "n") {
                println("\nDeseja continuar jogando? \n" +
                        "[S] Continuar\n" +
                        "[N] Sair")
                option = scanner.next().lowercase()
            }
            when (option) {
                "s" -> menu()
                "n" -> return printPodium(pairs)
            }
        }
        println("\nPartida $quantMatchs")
        val scanner = Scanner(System.`in`)
        try {
            print("\nInsira o valor que deseja apostar: ")
            var valor = scanner.nextInt()

            if (human.getMoney() != 0 && human.getMoney() >= valor) {
                human.placeBet(valor)

                for (pair in pairs) {
                    pair.placeBet(valor)
                }

                var generalBet = pairs.sumOf { it.doubleBet }
                println("Valor total da mesa: $generalBet")

                println("\nOPONENTES EM MESA\n")

                for (pair in pairs) {
                    pair.player1.hand.dealCard(deck, playedCards)
                    pair.player1.hand.dealCard(deck, playedCards)
                    pair.player2.hand.dealCard(deck, playedCards)
                    pair.player2.hand.dealCard(deck, playedCards)

                }
                println(pairs)

                for (pair in pairs) {
                    println("\nTurno da dupla: ${pair.name}")

                    for (player in listOf(pair.player1, pair.player2)) {
                        println("\nTurno de ${player.getName()}")
                        println("Mão atual: ${player.hand}")

                        if (pair.getDoubleScore() == 42) {
                            println("\nA partida acabou, o vencedor é o $pair.\nValor recebido: $generalBet (+20 por ser um Double Blackjack)")
                            pair.money += (generalBet + 20)
                            println(pair)
                            return
                        }

                        player.makeDecision(deck, playedCards)
                        generalBet = pairs.sumOf { it.doubleBet }

                        if (pair.getDoubleScore() == 42) {
                            println("\nA partida acabou, o vencedor é o $pair.\nValor recebido: $generalBet (+20 por ser um Double Blackjack)")
                            pair.money += (generalBet + 20)
                            println(pair)
                            return
                        }
                    }
                }

                val winners = findPairWinning(pairs)
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

    fun findPairWinning(pairList: List<Pair>): Pair? {
        return pairList
                .filter {
                    it.getDoubleScore() < 42 &&
                            it.player1.hand.getScore() < 21 &&
                            it.player2.hand.getScore() < 21
                }
                .maxByOrNull { it.getDoubleScore() }
    }

    private fun resetGame() {
        human.setMoney(Player().getMoney())
        human.hand = Hand()
        pairs = mutableListOf()
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

    fun printNormalResults() {

        val sortedResults = (listOf(human) + machines).sortedByDescending { it.getMoney() }
        writer.append("Podium:")
        println("Podium:")
        sortedResults.forEachIndexed { index, player ->
            val playerName = player.getName() ?: "Desconhecido"
            val money = player.getMoney()
            println("${index + 1}. $playerName - Dinheiro: $money")
            writer.newLine()
            writer.append("${index + 1}. $playerName - Dinheiro: $money")
        }
        println("Resultados salvos com sucesso.")

    }

    fun printPodium(pairs: List<Pair>) {
        val sortedPairs = pairs.sortedByDescending { it.money }

        writer.append("Podium:")
        println("Podium:")
        sortedPairs.forEachIndexed { index, pair ->
            val pairName = pair.name ?: "Desconhecido"
            val money = pair.money
            println("${index + 1}. $pairName - Dinheiro: $money")
            writer.newLine()
            writer.append("${index + 1}. $pairName - Dinheiro: $money")
        }
        println("Resultados salvos com sucesso.")
    }


    fun saveGames() {
        val arquivo = File("config.txt")
        arquivo.writeText(quantGames.toString())
    }

    fun loadGames(): Int {
        val arquivo = File("config.txt")
        return if (arquivo.exists()) {
            arquivo.readText().toIntOrNull() ?: 0
        } else {
            0
        }
    }
}
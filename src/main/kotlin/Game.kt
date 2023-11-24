package src.main.kotlin
import java.util.Scanner

class Game {
    private var wins = 0
    private var losses = 0
    private var pushes = 0
    private val player1 = Player()
    private val player2 = CPUPlayer()
    private val player3 = CPUPlayer()
    private val player4 = CPUPlayer()
    private val deck = Card.createDeck()

    init {

        deck.shuffle()
        start()
    }

    fun menu(){
        val scanner = Scanner(System.`in`)

        println("Escolha a quantidade de desafiantes:")
        println("[1] Desafiante. \n[2] Desafiantes. \n [3] Desafiantes.")
        var numberOfOpponents: Int = scanner.nextInt()

        if (numberOfOpponents in setOf(1,2,3)) {
            val opponents = mutableListOf<CPUPlayer>()
            for (i in 1..numberOfOpponents) {


                val opponent = CPUPlayer()
                opponent.setName("Oponente$i")
                opponents.add(opponent)
            }
        }else{

            println("Comando invalido")
            menu()
        }



        println("Escolha seu modo de jogo:")
        println("[1] Modo Normal. \n[2] Modo duplas. \n [3] Sair.")
        choice = scanner.next()

        if (choice == 1){


        } else if(choice == 2){

        }else if(choice == 3){

        }else{
            println("Comando invalido.")
            menu()
        }

        scanner.close()

    }


    private fun start() {

        player1.setName("Gabe")

        player1.getHand().dealCard(deck)
        player1.getHand().dealCard(deck)

        //println(player1.getStand())
        println(player1.stand())
        //println(player1.getStand())

        println(player1.getHand())

        println(deck.size)


    }
}

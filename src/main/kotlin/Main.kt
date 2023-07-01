import javax.swing.*
import java.awt.BorderLayout
import java.awt.Dimension
import kotlin.random.Random
import java.awt.Font

// TODO: Fancify appearance  --DONE
// TODO: Move reset and quit to separate line in south box? Maybe stacked?
// TODO: Move coins to far right and add coin image
// TODO: Add separate window to show previous guesses  --DONE
// TODO: Finish displayRules function  !IMPORTANT

class GameFrame : JFrame() {
    private var coins = 100
    private var currentGame: String? = null
    private var targetNumber: Int? = null
    private var attempts: Int = 0
    private var currentBet: Int? = null
    private val coinLabel: JLabel = JLabel("Coins: $coins")
    private val gameButtonGuess: JButton = JButton("Start Guess The Number Game")
    private val gameButtonDice: JButton = JButton("Start  Dice Duel Game")
    private val submitBetButton: JButton = JButton("Submit Bet")
    private val submitGuessButton: JButton = JButton("Submit Guess")
    private val resetButton: JButton = JButton("Reset")
    private val quitButton: JButton = JButton("Quit")
    private val rulesButton: JButton = JButton("Rules")
    private val inputField: JTextField = JTextField()
    private val outputArea: JTextArea = JTextArea()
    private val guessArea: JTextArea = JTextArea()


    init {
        // main screen details
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(800, 350)
        title = "Games"

        // screen for previous guesses
        guessArea.isEditable = false
        guessArea.isVisible = false
        guessArea.preferredSize = Dimension(150, 250)

        // input area for guesses and bets
        outputArea.isEditable = false
        inputField.preferredSize = Dimension(200, 50)
        inputField.font = Font("Arial", Font.BOLD, 20)
        // Allows player to press enter to submit
        inputField.addActionListener {
            if (submitBetButton.isEnabled) {
                submitBet()
            } else if (submitGuessButton.isEnabled) {
                submitGuess()
            }
        }

        // Button details
        gameButtonGuess.addActionListener { startGame("Guess the Number") }
        gameButtonDice.addActionListener { startGame("Dice Duel") }
        submitBetButton.addActionListener { submitBet() }
        submitGuessButton.addActionListener { submitGuess() }
        rulesButton.addActionListener { displayRules() }
        resetButton.addActionListener { resetGame() }
        quitButton.addActionListener { quitGame() }

        val topPanel = JPanel()
        topPanel.add(coinLabel)
        topPanel.add(gameButtonGuess)
        topPanel.add(gameButtonDice)
        topPanel.add(resetButton)
        topPanel.add(quitButton)

        val southPanel = JPanel()
        southPanel.add(rulesButton)
        southPanel.add(inputField)
        southPanel.add(submitBetButton)
        southPanel.add(submitGuessButton)


        layout = BorderLayout()
        add(topPanel, BorderLayout.NORTH)
        add(JScrollPane(outputArea), BorderLayout.CENTER)
        add(southPanel, BorderLayout.SOUTH)
        add(JScrollPane(guessArea), BorderLayout.EAST)

        disableSubmitButtons()
    }

    private fun quitGame() {
        val input = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the game?", "Quit", JOptionPane.YES_NO_OPTION)
        if (input == JOptionPane.YES_OPTION) {
            dispose()
        }
    }

    private fun resetGame() {
        val input = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset your coins?", "Reset", JOptionPane.YES_NO_OPTION)
        if (input == JOptionPane.YES_OPTION) {
            coins = 100
            coinLabel.text = "Coins: $coins"
            outputArea.append("Your coins have been reset.\n")
        }
    }

    private fun disableSubmitButtons() {
        submitBetButton.isEnabled = false
        submitGuessButton.isEnabled = false
    }

    private fun displayRules() {
        val rulesFrame = JFrame("Game Rules")
        rulesFrame.defaultCloseOperation = DISPOSE_ON_CLOSE
        rulesFrame.size = Dimension(550, 300)

        val tabbedPane = JTabbedPane()

        val diceDuelRules = JTextArea()
        diceDuelRules.isEditable = false
        diceDuelRules.append("Welcome to Dice Duel! Here are the rules:\n")
        diceDuelRules.append("1. You and the dealer each roll two dice.\n")
        diceDuelRules.append("2. If you roll a double 6, you automatically win triple your bet.\n")
        diceDuelRules.append("3. If you roll a double and the dealer doesn't, you win double your bet.\n")
        diceDuelRules.append("4. If both you and the dealer roll doubles, the one with the higher total wins double the bet.\n")
        diceDuelRules.append("5. If neither of you roll doubles, the one with the higher total wins the bet.\n")
        diceDuelRules.append("6. If the dealer rolls a double and you don't, the dealer wins.\n")
        diceDuelRules.append("7. If you and the dealer have the same total, it's a draw and you get your bet back.\n")
        diceDuelRules.append("8. If none of the above conditions are met, the dealer wins.\n")
        diceDuelRules.append("Good luck!\n")

        val guessNumberRules = JTextArea()
        guessNumberRules.isEditable = false
        guessNumberRules.append("Welcome to Guess the Number! Here are the rules:\n")
        guessNumberRules.append("1. You place a bet and then guess a number between 1 and 100.\n")
        guessNumberRules.append("2. You have a maximum of 5 attempts to guess the number.\n")
        guessNumberRules.append("3. If you guess the number correctly, you win double your bet.\n")
        guessNumberRules.append("4. If you don't guess the number in 5 attempts, you lose your bet.\n")
        guessNumberRules.append("Good luck!\n")

        tabbedPane.addTab("Guess the Number", JScrollPane(guessNumberRules))
        tabbedPane.addTab("Dice Duel", JScrollPane(diceDuelRules))

        rulesFrame.add(tabbedPane)
        rulesFrame.isVisible = true
    }

    private fun startGame(game: String) {
        currentGame = game
        outputArea.append("Starting $game game. \nPlease enter your bet.\n")
        submitBetButton.isEnabled = true
        if (game == "Guess the Number") {
            guessArea.text = ""  // Clear the guessArea
            guessArea.isVisible = true  // Make the guessArea visible
            guessArea.append("GUESSES\n")
        } else if (game == " Dice Duel") {
            guessArea.isVisible = false // Guess area is not needed for this game currently
        }
    }

    private fun updateCoins(amount: Int) {
        coins += amount
        coinLabel.text = "Coins: $coins"
    }

    private fun submitBet() {
        val bet = inputField.text.toIntOrNull()
        if (bet != null && bet in 1..coins) {
            currentBet = bet
            outputArea.append("Your bet is $bet coins.\n")
            outputArea.append("You are given a maximum of 5 guesses. \nIf you guess correctly you will be awarded double the amount of your bet!\n")
            when (currentGame) {
                "Guess the Number" -> {
                    targetNumber = Random.nextInt(1, 100)
                    outputArea.append("\nPlease enter your guess.\n")
                    submitGuessButton.isEnabled = true
                    submitBetButton.isEnabled = false
                }
                "Dice Duel" -> {
                    playDiceDuel()
                    submitBetButton.isEnabled = false
                }
            }
        } else {
            outputArea.append("Invalid bet! Please enter a number between 1 and your current coin balance.\n")
        }
        inputField.text = ""
    }

    private fun submitGuess() {
        val guess = inputField.text.toIntOrNull()
        inputField.text = ""
        if (guess != null) {
            attempts++
            if (guess == targetNumber) {
                guessArea.append("$attempts  :  $guess  :  Correct!\n")
                outputArea.append("Congratulations, you guessed the number! It took you $attempts attempts.\n")
                attempts = 0
                disableSubmitButtons()
                currentGame = null
                // update coins
                updateCoins(currentBet!!)
            } else {
                if (attempts < 5) {
                    if (guess < targetNumber!!) {
                        guessArea.append("$attempts  :  $guess  :  Low\n")
                        outputArea.append("Too low! Try again.\n")
                    } else {
                        guessArea.append("$attempts  :  $guess  :  High\n")
                        outputArea.append("Too high! Try again.\n")
                    }
                } else {
                    guessArea.append("$attempts  :  $guess  :  You Lost!\n")
                    outputArea.append("Sorry, you didn't guess the number. The correct number was $targetNumber\n")
                    attempts = 0
                    disableSubmitButtons()
                    currentGame = null
                    // update coins
                    updateCoins(-currentBet!!)
                }
            }
        } else {
            outputArea.append("Invalid input! Please enter a number.\n")
        }
    }

    private fun playDiceDuel() {
        val userRoll = Pair(Random.nextInt(1, 6), Random.nextInt(1, 6))
        val dealerRoll = Pair(Random.nextInt(1, 6), Random.nextInt(1, 6))

        outputArea.append("You rolled a ${userRoll.first} and a ${userRoll.second}.\n")

        when {
            userRoll.first + userRoll.second == 12 -> {
                outputArea.append("Double 6! You automatically win!\n")
                updateCoins(currentBet!! * 3)
            }
            userRoll.first == userRoll.second && dealerRoll.first != dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("You rolled a double. You win!\n")
                updateCoins(currentBet!! * 2)
            }
            userRoll.first == userRoll.second && dealerRoll.first == dealerRoll.second && userRoll.first + userRoll.second > dealerRoll.first + dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("Your total is higher than the dealer's. You win!\n")
                updateCoins(currentBet!! * 2)
            }
            userRoll.first + userRoll.second > dealerRoll.first + dealerRoll.second && dealerRoll.first != dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("Your total is higher than the dealer's. You win!\n")
                updateCoins(currentBet!!)
            }
            userRoll.first == userRoll.second && dealerRoll.first == dealerRoll.second && userRoll.first + userRoll.second < dealerRoll.first + dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("The dealer's double is higher. Dealer wins!\n")
                updateCoins(-currentBet!!)
            }
            userRoll.first != userRoll.second && dealerRoll.first == dealerRoll.second && userRoll.first + userRoll.second == dealerRoll.first + dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("The dealer rolled a double. Dealer wins!\n")
                updateCoins(-currentBet!!)
            }
            userRoll.first != userRoll.second && dealerRoll.first == dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("The dealer rolled a double. Dealer wins!\n")
                updateCoins(-currentBet!!)
            }
            userRoll.first + userRoll.second == dealerRoll.first + dealerRoll.second -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("It's a draw! You get your bet back.\n")
            }
            else -> {
                outputArea.append("The dealer rolled a ${dealerRoll.first} and a ${dealerRoll.second}.\n")
                outputArea.append("Dealer wins! You lose $currentBet coins.\n")
                updateCoins(-currentBet!!)
            }
        }
    }


}

fun main() {
    SwingUtilities.invokeLater {
        val frame = GameFrame()
        frame.isVisible = true
    }
}

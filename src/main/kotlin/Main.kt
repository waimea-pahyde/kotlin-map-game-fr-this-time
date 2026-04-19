import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*
import java.awt.Point
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.Timer
import kotlin.math.sin


/**
 * TODO:
 *      - app and then get the normal health and rnadomise
 *      - theres more notes about that somewhere but god knows where just scroll throughh
 *      - its one of the blue ones
 *
 *
 *
 *
 *      -
 */


fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val app = App()                 // Get an app state object
    val window = MainWindow(app)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


val map = mutableListOf<Location>()

/**
 * Manage app state
 *
 * @property name the user's name
 * @property score the points earned
 */
class App {

    var currentLocation: Location

    var currentPlayer: Player

    init {
        currentPlayer = Player()


        val cabin = Location("Cabin", "Creepy")
        val duckPond = Location("Duck Pond", "Ducky")

        val JerrysLens = Weapon("Jerry's Lens", "A frisbee-like lens, able to slice through enemy's with ease.", 1.5)
        val duckCatsBeak = Weapon("A Ducats Beak", "A sharp spear, able to pierce through enemys with the force of a flock of ducks.",
            2.0)

        val eyeOfJerru = Enemy("Jerry", 500, 500, true, 50, 10, JerrysLens, "Who are you and why have you invaded my cabin?!", "Alright, two can play that game.")
        val duckCat = Enemy("David the Duck Cat", 1000, 1000, true, 100, 10, duckCatsBeak, "Quack. QuackMeow. Quack quack quack.", "QUACKKKKK!")




//        Add Locations
        map.add(cabin)
        map.add(duckPond)

//      Add enemys to the location
        cabin.addEnemy(eyeOfJerru)
        duckPond.addEnemy(duckCat)




        currentLocation = map[0]
    }

    /**
     * fun current damage taken
     * Call current damage taken when calling take damage
     * then in main window get the value too
     */

    fun takeDamage(){

//      todo check if that's okay as it's only used once and it makes me laugh.

        currentLocation.listOfEnemies[0].calculateDamage(currentPlayer)

        if (currentLocation.listOfEnemies[0].alive) {
            currentLocation.listOfEnemies[0].enemyCurrentHP -= currentLocation.listOfEnemies[0].damageTaken
//
//          Checks if the enemy's health has gone below zero and if so sets it equal.
            if (currentLocation.listOfEnemies[0].enemyCurrentHP < 0) currentLocation.listOfEnemies[0].enemyCurrentHP = 0

//          checks if alive
            currentLocation.listOfEnemies[0].doIBreathe()


        } else if (!currentLocation.listOfEnemies[0].alive) {

//           If not alive, completes the location which signals the main window to unlock the buttons.
            currentLocation.complete = true

        }

//        Returning total damage for the dialogue

    }

    fun doPlayerDamage() {



        if (currentPlayer.currentHealth <= 0) {
            currentPlayer.alive = false
            return
        }
        val playerHitMultiplier = (10..50).random()
        var playerHit = (currentLocation.listOfEnemies[0].hitChange + playerHitMultiplier)



//        you see what I'm doing. FInsieh that
        if (playerHit > 50) {
            currentPlayer.currentHealth -= currentLocation.listOfEnemies[0].damage
        }

        if (currentLocation.complete) {
            currentPlayer.currentHealth = currentPlayer.health
        }


    }

    //  todo see if there's a nicer way to write this that isn't three lines long. I feel like there should be

    //  Invoked by a button in the Main window. If out of bounds, returns false so as the main window can display an error.
    fun goRight(): Boolean {
        if (map.indexOf(currentLocation) == map.size) return false
        var newLocation = map.indexOf(currentLocation)
        newLocation += 1


        currentLocation = map[newLocation]

        return true

    }

    fun goLeft(): Boolean {
        if (map.indexOf(currentLocation) == 0) return false

        var newLocation = map.indexOf(currentLocation)
        newLocation -= 1
        currentLocation = map[newLocation]
        return true
    }


}


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param app the app state object
 */
class MainWindow(val app: App) {
    val frame = JFrame("ggagagagagagaga change this later")
    private val panel = JPanel().apply { layout = null }

    private val titleLabel = JLabel(app.currentLocation.name)
    private val descLabel = JLabel(app.currentLocation.description)

//    ======Images=======
    private val duckBackground = ImageIcon(ClassLoader.getSystemResource("images/cabin.jpg"))
    val background = JLabel(duckBackground)


    private val infoLabel = JLabel()
    private val clickButton = JButton("Click Me!")
    private val infoButton = JButton("Info")

    private val infoWindow = InfoWindow(this, app)

    private val goRightButton = JButton("Go Right!")
    private val goLeftButton = JButton("Go Left!")
    private val outOfRangeError = JLabel("You can't go that way!")

    private val dialogueTimer = Timer(4000, null)

    private val playerDeathMessage = JLabel("You are dead")

    private val playerHealth = JLabel("${app.currentPlayer.currentHealth}/${app.currentPlayer.health}")

    private val dialogueMessage = JLabel("You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}")

    //    ==== ENEMY IN THE ROOM ====
    private val enemyName = JLabel(app.currentLocation.listOfEnemies[0].enemyName)
    private val enemyHealth =
        JLabel("${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}")

    private val enemyItself =
        JButton("I AM ENEMY AND WILL LATER BE REPLACED BY A POORLY CROPPED JPeG!!! I am ${app.currentLocation.listOfEnemies[0].status}")

    private val droppedWeapon = JButton(app.currentLocation.listOfEnemies[0].weaponDropped.name)

// todo
//    - add action listener on the enemy for when he's clicked with a random number for damage yada yada
//    - add action listener for a timer that goes off at a random-ish time and takes health from the player
//    - oh fuck I forgot about the player give him a class or something too
//    - then just like. do the same for him.
//    - worry about that later.


    // Pass app state to dialog too

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
        dialogueTimer.setRepeats(false)
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1194, 834)

        background.setBounds(0, 0,1194, 834)
        titleLabel.setBounds(30, 30, 340, 30)
        descLabel.setBounds(60, 30, 340, 30)
        infoLabel.setBounds(30, 90, 340, 30)

        infoButton.setBounds(300, 150, 70, 40)
        enemyName.setBounds(30, 50, 30, 30)
        enemyHealth.setBounds(30, 90, 300, 30)
        enemyItself.setBounds(500, 300, 500, 500)
        goRightButton.setBounds(100, 250, 100, 100)
        goLeftButton.setBounds(300, 250, 100, 100)
        outOfRangeError.setBounds(200, 250, 100, 100)
        playerHealth.setBounds(50, 50, 100, 100)

        droppedWeapon.setBounds(300, 200, 100, 100)

        playerDeathMessage.setBounds(250, 250, 100, 100)

        dialogueMessage.setBounds(250, 400, 1000, 100)



        panel.add(titleLabel)
        panel.add(infoLabel)
        panel.add(clickButton)
        panel.add(infoButton)
        panel.add(descLabel)
        panel.add(enemyName)
        panel.add(enemyHealth)
        panel.add(enemyItself)
        panel.add(goRightButton)
        panel.add(goLeftButton)
        panel.add(playerHealth)
        panel.add(background, JLayeredPane.DEFAULT_LAYER-1)


    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        clickButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        clickButton.background = Color(0xcc0055)

        infoButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
    }



    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {

        infoButton.addActionListener { handleInfoClick() }
        enemyItself.addActionListener { handleMainClick() }
//        playerNameInput.addActionListener { getPlayerName() }
        goRightButton.addActionListener { handleGoRightClick() }
        goLeftButton.addActionListener { handleGoLeftClick() }

        droppedWeapon.addActionListener { handleWeaponClick() }

        dialogueTimer.addActionListener { closeError() }

        dialogueTimer.addActionListener { closeDialogue() }

    }

    fun closeDialogue() {
        panel.remove(dialogueMessage)
        panel.revalidate()
        panel.repaint()
    }



    fun closeError() {
        panel.remove(outOfRangeError)
        panel.revalidate()
        panel.repaint()
    }

    fun showDialogue() {
        panel.add(dialogueMessage)
        dialogueTimer.start()
        panel.revalidate()
        panel.repaint()
    }

    private fun handleWeaponClick(){
        app.currentPlayer.currentWeapon = app.currentLocation.listOfEnemies[0].weaponDropped
        dialogueMessage.text = "You picked up ${app.currentLocation.listOfEnemies[0].weaponDropped.name}"
        panel.add(dialogueMessage)
        dialogueTimer.start()
        panel.remove(droppedWeapon)
        panel.revalidate()
        panel.repaint()
    }
    private fun handleMainClick() {


        updateUI()
        app.takeDamage()       // Update the app state
        updateUI()
        makeButtonShake(enemyItself)
        showDialogue()

        app.doPlayerDamage()
        updateUI()
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    private fun handleGoRightClick() {
        val inRange = app.goRight()
        when (inRange) {
            true -> {
                updateUI()
                dialogueMessage.text = "You travel right, and arrive at the ${app.currentLocation.name}, a ${app.currentLocation.description}."
                showDialogue()
            }
            false -> {
                panel.add(outOfRangeError)
                panel.setComponentZOrder(outOfRangeError, 1)
                panel.revalidate()
                panel.repaint()
                updateUI()

            }
        }
        updateUI()
    }

    private fun handleGoLeftClick() {
        val inRange = app.goLeft()

        when (inRange) {
            true -> updateUI()
            false -> {
                panel.add(outOfRangeError)
                dialogueTimer.start()
                panel.setComponentZOrder(outOfRangeError, 1)
                panel.revalidate()
                panel.repaint()
                updateUI()
            }
        }
        updateUI()
    }


    fun makeButtonShake(button: JButton) {
        val originalLocation: Point = button.location
        val shakeDistance = 10
        val shakeDuration = 400 // ms
        val shakeFrequency = 20 // ms per frame
        val startTime = System.currentTimeMillis()

        Timer(shakeFrequency) { timerEvent ->
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed > shakeDuration) {
                button.location = originalLocation
                (timerEvent.source as Timer).stop()
            } else {
                // Calculate sine wave movement
                val offset = (sin(elapsed.toDouble() * 0.1) * shakeDistance).toInt()
                button.setLocation(originalLocation.x + offset, originalLocation.y)
            }
        }.start()
    }


    private fun endGame() {
        panel.remove(titleLabel)
        panel.remove(infoLabel)
        panel.remove(clickButton)
        panel.remove(infoButton)
        panel.remove(descLabel)
        panel.remove(enemyName)
        panel.remove(enemyHealth)
        panel.remove(enemyItself)
        panel.remove(goRightButton)
        panel.remove(goLeftButton)
        panel.add(playerDeathMessage)
        panel.revalidate()
        panel.repaint()
    }

//    todo either a. fix this. I dont want to do that. b. remove it. i don't want to do that either. we live in limbo.
//    fun getPlayerName(): String {
//        val playerName: String = playerNameInput.toString()
//        return playerName
//
//    }


    //    todo add an if room is not complete
    fun updateUI() {

        dialogueMessage.text = "You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}!"

        if (!app.currentPlayer.alive) endGame()

        playerHealth.text = "${app.currentPlayer.currentHealth}/${app.currentPlayer.health}"

        droppedWeapon.text = app.currentLocation.listOfEnemies[0].weaponDropped.name


        titleLabel.text = app.currentLocation.name


        when (app.currentLocation.complete) {

            false -> {
                goRightButton.isEnabled = false
                goLeftButton.isEnabled = false
                enemyItself.isEnabled = true
            }

            true -> {
                goRightButton.isEnabled = true
                goLeftButton.isEnabled = true
                enemyItself.isEnabled = false
                dialogueMessage.text = "You killed ${app.currentLocation.listOfEnemies[0].enemyName}!"

                panel.add(droppedWeapon)
                panel.revalidate()
                panel.repaint()
            }
        }

        enemyName.text = (app.currentLocation.listOfEnemies[0].enemyName)
        enemyHealth.text =
            "I AM ENEMY AND WILL LATER BE REPLACED BY A POORLY CROPPED JPeG!!! I am ${app.currentLocation.listOfEnemies[0].status}"


        enemyHealth.text =
            "${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}"

        infoWindow.updateUI()       // Keep child dialog window UI up-to-date too
    }

    fun show() {
        frame.isVisible = true
    }


}


/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class InfoWindow(val owner: MainWindow, val app: App) {
    private val dialog = JDialog(owner.frame, "DIALOG TITLE", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()
    private val resetButton = JButton("Reset")

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(240, 180)

        infoLabel.setBounds(30, 30, 180, 60)
        resetButton.setBounds(30, 120, 180, 30)

        panel.add(infoLabel)
        panel.add(resetButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        resetButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()
    }

    private fun setupActions() {
        resetButton.addActionListener { handleResetClick() }
    }

    private fun handleResetClick() {
//        app.resetScore()    // Update the app state
        owner.updateUI()    // Update the UI to reflect this, via the main window
    }

    fun updateUI() {
        // Use app properties to display state
//        infoLabel.text = "<html>User: ${app.name}<br>Score: ${app.score} points"

//        resetButton.isEnabled = app.score > 0
    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window
        dialog.setLocation(                           // Position next to main window
            ownerBounds.x + ownerBounds.width + 10,
            ownerBounds.y
        )

        dialog.isVisible = true
    }
}

class Enemy(
    val enemyName: String,
    val enemyMaxHP: Int,
    var enemyCurrentHP: Int,
    var alive: Boolean = true,

    val damage: Int,

    val hitChange: Int,

    val weaponDropped: Weapon,



    val dialog1: String,
    val dialog2: String,

    var damageTaken: Int = 0,
    ) {
    var status = ""

    //    Checks if alive
    fun doIBreathe() {
        if (enemyCurrentHP == 0) {
            alive = false
        }
//    Changes the status to plaintext for ui reasons
        if (alive) {
            status = "alive"
        } else status = "dead"

    }

    fun calculateDamage(player: Player) {
        val randomDamageElementForReplayability = (10..50).random()
        val totalDamage = (player.calculateDamage() + randomDamageElementForReplayability)
        damageTaken = totalDamage

    }
}

class Location(

    val name: String,
    val description: String,
    var complete: Boolean = false
) {
    val listOfEnemies = mutableListOf<Enemy>()
    fun addEnemy(enemy: Enemy) {
        listOfEnemies.add(enemy)
    }

}

class Player(
    val name: String = "John",

    val baseDamage: Int = 100,
    val health: Int = 1000,
    var currentHealth: Int = 1000,
    var alive: Boolean = true,
    var currentWeapon: Weapon = Weapon("Sharp Stick", "Not much worse than being poked in the eye with this", 1.0)
) {





    fun calculateDamage(): Int {
        val finalDamage = baseDamage * currentWeapon.damageMultiplier.toInt()
        return finalDamage
    }
}

class Weapon (
    val name: String,
    val description: String,
    val damageMultiplier: Double,
)


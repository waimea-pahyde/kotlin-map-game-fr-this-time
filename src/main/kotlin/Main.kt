import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Font
import java.awt.Point
import javax.swing.*
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

fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))

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
        val duckCatsBeak = Weapon(
            "A Ducats Beak", "A sharp spear, able to pierce through enemys with the force of a flock of ducks.",
            2.0
        )

        val eyeOfJerru = Enemy(
            "Jerry",
            500,
            500,
            ImageIcon(ClassLoader.getSystemResource("images/cabin.jpg")),
            ImageIcon(ClassLoader.getSystemResource("images/jerry.png")),
            true,
            50,
            10,
            JerrysLens,

            )
        val duckCat = Enemy(
            "David the Duck Cat",
            1000,
            1000,
            ImageIcon(ClassLoader.getSystemResource("images/duckLake.png")),
            ImageIcon(ClassLoader.getSystemResource("images/duckCat.png")),
            true,
            100,
            10,
            duckCatsBeak,
        )


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

    fun takeDamage() {


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
        if (map.indexOf(currentLocation) + 1 !in map.indices) return false
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


    //    ======Images=======
    private val jerryBackground = ImageIcon(ClassLoader.getSystemResource("images/cabin.jpg"))
    val background = JLabel(jerryBackground)

    val leftArrow = ImageIcon(ClassLoader.getSystemResource("images/arrowPointingRight.png"))

    private val titleBarImage = ImageIcon(ClassLoader.getSystemResource("images/titleBar.png"))
    val titleBackground = JLabel(titleBarImage)

    private val goRightButton =
        JButton(ImageIcon(ClassLoader.getSystemResource("images/arrow.png")).scaled(90, 90))
    private val goLeftButton = JButton(leftArrow.scaled(90, 90))


    private val outOfRangeError = JLabel("You can't go that way!")

    private val dialogueTimer = Timer(4000, null)
    private val titleScreenTimer = Timer(10000, null)

    private val playerDeathMessage = JLabel("You are dead")

    private val playerHealth = JLabel("${app.currentPlayer.currentHealth}/${app.currentPlayer.health}")
    private val playerHealthBar =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/playerHealth.png")).scaled(300, 500))


    //    ==== ENEMY IN THE ROOM ====
    private val enemyName = JLabel(app.currentLocation.listOfEnemies[0].enemyName)

    private val enemyHealthBar =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/enemyHealth.png")).scaled(600, 400))
    private val enemyHealth =
        JLabel("${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}")


    private val enemyItself =
        JButton(app.currentLocation.listOfEnemies[0].enemyForm.scaled(500, 500))

    private val droppedWeapon = JButton(app.currentLocation.listOfEnemies[0].weaponDropped.name)

    //    === DIALOGUE BOXES === //
    private val placeDialogue = JLabel("This is a place")
    private val placeDialogueBackground =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/transparentDialogue.png")).scaled(600, 300))

    private val damageDialogueMessage =
        JLabel("You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}")
    private val damageDialogueBox =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/damageDialogue.png")).scaled(600, 250))


//    ======================

    private val winText = JLabel("You won!!")


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

        background.setBounds(0, 0, 1194, 834)

        titleLabel.setBounds(520, 40, 340, 30)
        titleBackground.setBounds(230, 10, 700, 100)

        goRightButton.setBounds(250, 10, 100, 100)
        goLeftButton.setBounds(800, 15, 100, 100)


        enemyName.setBounds(30, 50, 30, 30)


        enemyHealth.setBounds(50, 625, 300, 30)
        enemyHealthBar.setBounds(-100, 600, 500, 100)

        playerHealth.setBounds(50, 520, 300, 30)
        playerHealthBar.setBounds(-150, 500, 500, 100)


        enemyItself.setBounds(300, 200, 500, 500)

        placeDialogue.setBounds(430, 350, 300, 50)
        placeDialogueBackground.setBounds(330, 230, 600, 300)

        outOfRangeError.setBounds(200, 250, 100, 100)




        droppedWeapon.setBounds(300, 200, 100, 100)

        playerDeathMessage.setBounds(250, 250, 100, 100)
        winText.setBounds(250, 250, 100, 100)


        damageDialogueMessage.setBounds(480, 150, 1000, 100)
        damageDialogueBox.setBounds(370, 100, 400, 200)






        panel.add(enemyName, JLayeredPane.DEFAULT_LAYER)
        panel.add(enemyHealth, JLayeredPane.DEFAULT_LAYER)
        panel.add(enemyHealthBar, JLayeredPane.DEFAULT_LAYER - 1)

        panel.add(enemyItself, JLayeredPane.DEFAULT_LAYER)
        panel.add(goRightButton, JLayeredPane.DEFAULT_LAYER)
        panel.add(goLeftButton, JLayeredPane.DEFAULT_LAYER)

        panel.add(playerHealth, JLayeredPane.DEFAULT_LAYER)
        panel.add(playerHealthBar, JLayeredPane.DEFAULT_LAYER - 1)

        panel.add(titleLabel, JLayeredPane.DEFAULT_LAYER + 1)
        panel.add(titleBackground, JLayeredPane.DEFAULT_LAYER - 1)

        panel.add(background, JLayeredPane.DEFAULT_LAYER - 1)


    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)

        enemyItself.isBorderPainted = false
        enemyItself.isFocusPainted = false
        enemyItself.isContentAreaFilled = false

        goLeftButton.isFocusPainted = false
        goRightButton.isFocusPainted = false
        goLeftButton.isContentAreaFilled = false
        goRightButton.isContentAreaFilled = false
        goLeftButton.isBorderPainted = false
        goRightButton.isBorderPainted = false
    }


    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {

        enemyItself.addActionListener { handleMainClick() }

        goRightButton.addActionListener { handleGoRightClick() }
        goLeftButton.addActionListener { handleGoLeftClick() }

        droppedWeapon.addActionListener { handleWeaponClick() }

        dialogueTimer.addActionListener { closeError() }

        dialogueTimer.addActionListener { closeDialogue() }

    }

    fun titleScreen() {
        panel.removeAll()
        var titleScreen = mutableListOf<ImageIcon>()
        val titleScreen1 = ImageIcon(ClassLoader.getSystemResource("images/titleScreen1.png"))
        val titleScreen2 = ImageIcon(ClassLoader.getSystemResource("images/titleScreen2.png"))
        val titleScreen3 = ImageIcon(ClassLoader.getSystemResource("images/titleScreen3.png"))
        val titleScreen4 = ImageIcon(ClassLoader.getSystemResource("images/titleScreen4.png"))
        val titleScreen5 = ImageIcon(ClassLoader.getSystemResource("images/titleScreen5.png"))
        titleScreen.add(titleScreen1)
        titleScreen.add(titleScreen2)
        titleScreen.add(titleScreen3)
        titleScreen.add(titleScreen4)
        titleScreen.add(titleScreen5)

        for (screen in titleScreen) {

        }


    }


    fun closeDialogue() {
        panel.remove(damageDialogueMessage)
        panel.remove(damageDialogueBox)
        panel.remove(placeDialogueBackground)
        panel.remove(placeDialogue)

        panel.revalidate()
        panel.repaint()
    }


    fun closeError() {
        panel.remove(outOfRangeError)
        panel.revalidate()
        panel.repaint()
    }

    fun showDialogue() {
        panel.add(damageDialogueBox, JLayeredPane.DEFAULT_LAYER)
        panel.add(damageDialogueMessage, JLayeredPane.DEFAULT_LAYER)


        dialogueTimer.start()
        panel.revalidate()
        panel.repaint()
    }

    fun showPlaceDialogue() {
        panel.remove(damageDialogueMessage)
        panel.remove(damageDialogueBox)
        panel.add(placeDialogue, JLayeredPane.DEFAULT_LAYER)
        panel.add(placeDialogueBackground, JLayeredPane.DEFAULT_LAYER + 1)
        dialogueTimer.start()
        panel.revalidate()
        panel.repaint()


    }


    private fun handleWeaponClick() {
        app.currentPlayer.currentWeapon = app.currentLocation.listOfEnemies[0].weaponDropped
        damageDialogueMessage.text = "You picked up ${app.currentLocation.listOfEnemies[0].weaponDropped.name}"
        panel.add(damageDialogueMessage)
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

        winGameCheck()

        app.doPlayerDamage()
        updateUI()
    }


    private fun handleGoRightClick() {
        val inRange = app.goRight()
        when (inRange) {
            true -> {
                updateUI()
                placeDialogue.text =
                    "You travel left, and arrive at the ${app.currentLocation.name}, a ${app.currentLocation.description}."
                showPlaceDialogue()
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
        val shakeDuration = 400
        val shakeFrequency = 20
        val startTime = System.currentTimeMillis()

        Timer(shakeFrequency) { timerEvent ->
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed > shakeDuration) {
                button.location = originalLocation
                (timerEvent.source as Timer).stop()
            } else {

                val offset = (sin(elapsed.toDouble() * 0.1) * shakeDistance).toInt()
                button.setLocation(originalLocation.x + offset, originalLocation.y)
            }
        }.start()
    }

    private fun endGame() {
        panel.remove(titleLabel)

        panel.remove(enemyName)
        panel.remove(enemyHealth)
        panel.remove(enemyItself)
        panel.remove(goRightButton)
        panel.remove(goLeftButton)
        panel.add(playerDeathMessage)
        panel.revalidate()
        panel.repaint()
    }

    fun winGameCheck() {
        for (location in map) {
            if (!location.complete) return
        }
        winGame()
    }

    fun winGame() {
        panel.removeAll()
        panel.add(winText)
        panel.revalidate()
        panel.repaint()


    }


    fun updateUI() {

        damageDialogueMessage.text =
            "You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}!"

        if (!app.currentPlayer.alive) endGame()

        playerHealth.text = "${app.currentPlayer.currentHealth}/${app.currentPlayer.health}"

        droppedWeapon.text = app.currentLocation.listOfEnemies[0].weaponDropped.name

        background.icon = (app.currentLocation.listOfEnemies[0].background)
        enemyItself.icon = (app.currentLocation.listOfEnemies[0].enemyForm)


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
                damageDialogueMessage.text = "You killed ${app.currentLocation.listOfEnemies[0].enemyName}!"

                panel.add(droppedWeapon, JLayeredPane.DEFAULT_LAYER + 1)
                panel.revalidate()
                panel.repaint()
            }
        }

        enemyName.text = (app.currentLocation.listOfEnemies[0].enemyName)
        enemyHealth.text =
            "I AM ENEMY AND WILL LATER BE REPLACED BY A POORLY CROPPED JPeG!!! I am ${app.currentLocation.listOfEnemies[0].status}"


        enemyHealth.text =
            "${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}"

        // Keep child dialog window UI up-to-date too
    }

    fun show() {
        frame.isVisible = true
    }


}


class Enemy(
    val enemyName: String,
    val enemyMaxHP: Int,
    var enemyCurrentHP: Int,

    val background: ImageIcon,
    val enemyForm: ImageIcon,


    var alive: Boolean = true,

    val damage: Int,

    val hitChange: Int,

    val weaponDropped: Weapon,


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

class Weapon(
    val name: String,
    val description: String,
    val damageMultiplier: Double,
)


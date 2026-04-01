import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*


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

        val eyeOfJerru = Enemy("Jerry", 10000, 10000, true, 300, 10)
        val duckCat = Enemy("David the Duck Cat", 10000, 10000, true, 300, 10)

//        Add Locations
        map.add(cabin)
        map.add(duckPond)

//      Add enemys to the location
        cabin.addEnemy(eyeOfJerru)
        duckPond.addEnemy(duckCat)




        currentLocation = map[0]
    }


    fun takeDamage() {
        if (currentLocation.listOfEnemies[0].alive) {
            currentLocation.listOfEnemies[0].enemyCurrentHP -= currentPlayer.calculateDamage()
            currentLocation.listOfEnemies[0].doIBreathe()

        } else if (!currentLocation.listOfEnemies[0].alive) {
            currentLocation.complete = true

        }

    }

    fun doPlayerDamage() {
        if (currentPlayer.currentHealth <= 0) {
            currentPlayer.alive = false
            return
        }
        val playerHitMultiplier = (10..50).random()
        var playerHit = (currentLocation.listOfEnemies[0].hitChange + playerHitMultiplier)


//        you see what I'm doing. FInsieh that
        when (playerHit) {
            >50 ->
        }

        currentPlayer.currentHealth -= currentLocation.listOfEnemies[0].damage

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


    private val infoLabel = JLabel()
    private val clickButton = JButton("Click Me!")
    private val infoButton = JButton("Info")

    private val infoWindow = InfoWindow(this, app)

    private val goRightButton = JButton("Go Right!")
    private val goLeftButton = JButton("Go Left!")
    private val outOfRangeError = JLabel("You can't go that way!")

    private val errorTimer = Timer(1000, null)

    private val playerDeathMessage = JLabel("You are dead")

    private val playerHealth = JLabel("${app.currentPlayer.currentHealth}/${app.currentPlayer.health}")


    //    ==== ENEMY IN THE ROOM ====
    private val enemyName = JLabel(app.currentLocation.listOfEnemies[0].enemyName)
    private val enemyHealth =
        JLabel("${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}")

    private val enemyItself =
        JButton("I AM ENEMY AND WILL LATER BE REPLACED BY A POORLY CROPPED JPeG!!! I am ${app.currentLocation.listOfEnemies[0].status}")


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
        errorTimer.setRepeats(false)
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1080, 1440)

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

        playerDeathMessage.setBounds(250, 250, 100, 100)



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

        errorTimer.addActionListener { closeError() }

    }

    fun closeError() {
        panel.remove(outOfRangeError)
        panel.revalidate()
        panel.repaint()
    }

    private fun handleMainClick() {
        updateUI()
        app.takeDamage()       // Update the app state
        updateUI()
        app.doPlayerDamage()
        updateUI()
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    private fun handleGoRightClick() {
        val inRange = app.goRight()
        when (inRange) {
            true -> updateUI()
            false -> {
                println("i am false")
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
        println("lalalalalalalalal")
        println(inRange)
        when (inRange) {
            true -> updateUI()
            false -> {
                panel.add(outOfRangeError)
                println("fdsfdsfdsfsdfdfsfdsdfd")
                errorTimer.start()
                panel.setComponentZOrder(outOfRangeError, 1)
                panel.revalidate()
                panel.repaint()
                updateUI()
            }
        }
        updateUI()
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

        if (!app.currentPlayer.alive) endGame()

        playerHealth.text = "${app.currentPlayer.currentHealth}/${app.currentPlayer.health}"


        titleLabel.text = app.currentLocation.name


        when (app.currentLocation.complete) {

            false -> {
                goRightButton.isEnabled = false
                goLeftButton.isEnabled = false
            }

            true -> {
                goRightButton.isEnabled = true
                goLeftButton.isEnabled = true
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
    var damageMultiplier: Int = 1,
    val baseDamage: Int = 100,
    val health: Int = 1000,
    var currentHealth: Int = 1000,
    var alive: Boolean = true
) {
    fun calculateDamage(): Int {
        val finalDamage = baseDamage * damageMultiplier
        return finalDamage
    }
}



import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*


/**
 * TODO:
 *      - 149 to 57 something
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

//TODO
//    - in location - DONE you can now be in a location
//    - enemy spawns - Enemy SPAWNS DONE
//    - show enemy - THOSE ARE THE SAME THINGAS
//    - show health bar DONE
//    - doesDamage
//    - if health = zero alive=false
//    if roomenemy.isAlive = false button = unblock button yippeee
//    - if wendigo is dead yippe you win good job kid
//


    var currentLocation: Location

    var currentPlayer: Player

    init {
        val currentPlayer = Player()


        val cabin = Location("Cabin", "Creepy")
        val duckPond = Location("Duck", "Duck")

        val eyeOfJerru = Enemy("Jerry", 500000, 500000)

//        Add Locations
        map.add(cabin)
        map.add(duckPond)

        cabin.addEnemy(eyeOfJerru)
        print(cabin.listOfEnemies)


        currentLocation = map[0]
    }


    //    todo check if he's dead up at the  location thing and if he is make him dissapear. Or turn him into a corpse
//    road kill eyeball carcuses are delicious
//    while enemy isn't dead or whatever they aren't paying me for this
    fun takeDamage() {
        while (currentLocation.listOfEnemies[0].enemyCurrentHP != 0) {
            currentLocation.listOfEnemies[0].enemyCurrentHP -= c

        }



        if (currentLocation.listOfEnemies[0].enemyCurrentHP == 0) {
            currentLocation.listOfEnemies[0].alive = false
            currentLocation.listOfEnemies[0].doIBreathe()


        }
    }


    fun doIBreathe() {
        if (currentLocation.listOfEnemies[0].alive) {
            currentLocation.listOfEnemies[0].status = "alive"
        } else currentLocation.listOfEnemies[0].status = "dead"

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

    private val playerNameInput = JTextField(20)


    private val infoLabel = JLabel()
    private val clickButton = JButton("Click Me!")
    private val infoButton = JButton("Info")

    private val infoWindow = InfoWindow(this, app)

    private val enemyName = JLabel(app.currentLocation.listOfEnemies[0].enemyName)
    private val enemyHealth =
        JLabel("${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}")

    private val enemyItself =
        JButton("I AM ENEMY AND WILL LATER BE REPLACED BY A POORLY CROPPED JPeG!!! I am ${app.currentLocation.listOfEnemies[0].status}")


// todo
//    - show enemy health whatever out of whatever
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
        playerNameInput.setBounds(200, 150, 50, 50)

        panel.add(titleLabel)
        panel.add(infoLabel)
        panel.add(clickButton)
        panel.add(infoButton)
        panel.add(descLabel)
        panel.add(enemyName)
        panel.add(enemyHealth)
        panel.add(enemyItself)
        panel.add(playerNameInput)
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
        playerNameInput.addActionListener { getPlayerName() }

    }

    private fun handleMainClick() {


        app.takeDamage()       // Update the app state
        updateUI()                  // Update this window UI to reflect this
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    fun getPlayerName(): String {
        val playerName: String = playerNameInput.toString()
        return playerName

    }

    fun updateUI() {
//        infoLabel.text = "User ${app.name} has ${app.score} points"

//        if (app.maxScoreReached()) {
//            clickButton.text = "No More!"
//            clickButton.isEnabled = false
//        } else {
//            clickButton.text = "Click Me!"
//            clickButton.isEnabled = true
//        }

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
    var alive: Boolean = true


) {
    var status = ""

    //    Probably unnecasary, but checks the boolean and sets the status to be 'alive/dead' in plaintext
    fun doIBreathe() {
        if (alive) {
            status = "alive"
        } else status = "dead"

    }
}

class Location(

    val name: String,
    val description: String,
) {
    val listOfEnemies = mutableListOf<Enemy>()
    fun addEnemy(enemy: Enemy) {
        listOfEnemies.add(enemy)
    }

}

class Player(
    val name: String,
    var damageMultiplier: Int = 1,
    val baseDamage: Int = 100,
) {

}



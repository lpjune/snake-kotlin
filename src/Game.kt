
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer

class Game : ActionListener, JPanel() {

    private val windowHeight = 300
    private val windowWidth = 300
    private val biteSize = 10
    private val allDots = 900
    private val randomPosition = 29
    private var timer: Timer? = null
    private var running = true

    private val x = IntArray(allDots)
    private val y = IntArray(allDots)

    private var tailLength: Int = 0
    private var biteX: Int = 0
    private var biteY: Int = 0
    private var points: Int = 0

    private var left = false
    private var right = true
    private var up = false
    private var down = false

    private var head: Image? = null
    private var tail: Image? = null
    private var bite: Image? = null

    private var keyListener = object : KeyAdapter() {
        override fun keyPressed(keyEvent: KeyEvent) {
            val k = keyEvent.keyCode

            if (k == KeyEvent.VK_LEFT && !right) {
                left = true
                up = false
                down = false
            }

            if (k == KeyEvent.VK_RIGHT && !left) {
                right = true
                up = false
                down = false
            }

            if (k == KeyEvent.VK_UP && !down) {
                up = true
                left = false
                right = false
            }

            if (k == KeyEvent.VK_DOWN && !up) {
                down = true
                left = false
                right = false
            }

            if (k == KeyEvent.VK_SPACE && !running) {
                restart()
            }
        }
    }


    init {
        addKeyListener(keyListener)
        background = Color.black
        isFocusable = true
        preferredSize = Dimension(windowWidth, windowHeight)
        getImages()
        startGame()
    }

    private fun getImages() {
        head = ImageIcon("src/img/head.png").image
        tail = ImageIcon("src/img/tail.png").image
        bite = ImageIcon("src/img/bite.png").image
    }

    private fun startGame() {
        tailLength = 3

        for (z in 0 until tailLength) {
            x[z] = 50 - z * 10
            y[z] = 50
        }
        newBite()
        timer = Timer(140, this)
        timer?.start()
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        draw(graphics)
    }

    private fun draw(graphics: Graphics) {
        if (running) {
            graphics.drawImage(bite, biteX, biteY, this)
            for (z in 0 until tailLength) {
                if (z == 0) {
                    graphics.drawImage(head, x[z], y[z], this)
                } else {
                    graphics.drawImage(tail, x[z], y[z], this)
                }
            }
            Toolkit.getDefaultToolkit().sync()

        } else {
            gameOver(graphics)
        }
    }

    private fun gameOver(graphics: Graphics) {
        var msg = "Welp, ya lost"
        var msg2 = "Press [space] to restart"
        val small = Font("Helvetica", Font.BOLD, 14)
        val fontMetrics = getFontMetrics(small)

        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY

        (graphics as Graphics2D).setRenderingHints(rh)

        graphics.color = Color.white
        graphics.font = small
        graphics.drawString(msg, (windowWidth - fontMetrics.stringWidth(msg)) / 2, windowHeight / 3)
        graphics.drawString(msg2, (windowWidth - fontMetrics.stringWidth(msg2)) / 2, windowHeight / 2)
    }


    fun restart() {
        running = true
        // fix timer issue
        startGame()
    }

    private fun gotBite() {
        if (x[0] == biteX && y[0] == biteY) {
            tailLength++
            newBite()
            addPoints()
        }
    }

    private fun move() {
        for (z in tailLength downTo 1) {
            x[z] = x[z - 1]
            y[z] = y[z - 1]
        }
        if (left) {
            x[0] -= biteSize
        }
        if (right) {
            x[0] += biteSize
        }
        if (up) {
            y[0] -= biteSize
        }
        if (down) {
            y[0] += biteSize
        }
    }

    private fun collision() {
        for (z in tailLength downTo 1) {
            if (z > 4 && x[0] == x[z] && y[0] == y[z]) {
                running = false
            }
        }
        if (y[0] >= windowHeight) {
            running = false
        }
        if (y[0] < 0) {
            running = false
        }

        if (x[0] >= windowWidth) {
            running = false
        }

        if (x[0] < 0) {
            running = false
        }

        if (!running) {
            timer!!.stop()
        }
    }

    private fun newBite() {
        var r = (Math.random() * randomPosition).toInt()
        biteX = r * biteSize

        r = (Math.random() * randomPosition).toInt()
        biteY = r * biteSize
    }

    private fun addPoints() {
        points+=10
    }

    private fun displayPoints() {

    }

    override fun actionPerformed(e: ActionEvent?) {
        if (running) {
            gotBite()
            collision()
            move()
        }
        repaint()
    }

}
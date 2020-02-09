import java.awt.EventQueue
import javax.swing.JFrame

class Main : JFrame() {

    init {
        initUI()
    }

    private fun initUI() {

        add(Game())

        title = "Kotlin Snake"
        isResizable = false

        pack()

        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            EventQueue.invokeLater {
                val ex = Main()
                ex.isVisible = true
            }
        }
    }
}
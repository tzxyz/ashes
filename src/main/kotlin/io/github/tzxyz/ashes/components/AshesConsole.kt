package io.github.tzxyz.ashes.components

import io.github.tzxyz.ashes.models.AshesConnection
import io.github.tzxyz.ashes.redis.AshesRedisRawCommand
import io.github.tzxyz.ashes.services.AshesConsoleService
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import tornadofx.vgrow

class AshesConsole: StyleClassedTextArea() {

    var connection: AshesConnection? = null
    var service: AshesConsoleService? = null
    private val context = CommandContext()

    fun init() {
        paragraphGraphicFactory = LineNumberFactory.get(this)
        isWrapText = true
        vgrow = Priority.ALWAYS
        val prefix = "${connection?.host}:${connection?.port}> "
        appendText(prefix)
        setOnKeyPressed { e ->
            if (e.code == KeyCode.ENTER && !e.isShiftDown) {
                val text = text.replace(prefix, "")
                val line = text.split("\n").findLast { it != "" }.orEmpty()
                context.addCommand(line)
                val result = service?.execute(AshesRedisRawCommand(line))
                if (result!!.isNotBlank()) {
                    appendText("$result\n")
                }
                appendText(prefix)

            }
        }
        addEventFilter(KeyEvent.KEY_PRESSED) { e ->
            if (e.code == KeyCode.BACK_SPACE && text.endsWith("redis:> ")) {
                e.consume()
            }
            if (e.code == KeyCode.UP) {
                e.consume()
                appendText(context.prev())
            }
            if (e.code == KeyCode.DOWN) {
                e.consume()
                appendText(context.next())
            }
            if (e.code == KeyCode.BACK_SPACE && text.endsWith(prefix)) {
                e.consume()
            }
            if (e.code == KeyCode.L && e.isControlDown) {
                replaceText(prefix)
            }
        }
    }

    class CommandContext {
        private val commands = arrayListOf<String>()
        private var queryIndex = 0

        fun addCommand(command: String) {
            commands.add(command)
            queryIndex = commands.size - 1
        }

        fun prev(): String {
            val command = commands[queryIndex]
            queryIndex = if (queryIndex == 0) queryIndex else queryIndex - 1
            return command
        }

        fun next():String {
            val command = commands[queryIndex]
            queryIndex = if (queryIndex == commands.size - 1) queryIndex else queryIndex + 1
            return command
        }
    }
}

package io.github.tzxyz.ashes.views

import io.github.tzxyz.ashes.global.Current
import io.github.tzxyz.ashes.redis.AshesRedisClientFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import tornadofx.vbox
import tornadofx.vgrow

class AshesConsoleView: AshesBaseView() {

    private val client = AshesRedisClientFactory.get(Current.getConnection())

    override val root = vbox {
        add(console())
    }

    private fun console(): StyleClassedTextArea {
        val console = StyleClassedTextArea()
        console.paragraphGraphicFactory = LineNumberFactory.get(console)
        console.isWrapText = true
        console.vgrow = Priority.ALWAYS
        console.appendText("redis:> ")
        console.setOnKeyPressed { e ->
            if (e.code == KeyCode.ENTER && !e.isShiftDown) {
                val text = console.text.replace("redis:> ", "")
                val line = text.split("\n").findLast { it != "" }.orEmpty()
                val (command, args) = line.split(" ")
                println(command)
                println(args)
                console.appendText("redis:> ")
            }
        }
        return console
    }
}
package io.github.tzxyz.ashes.views

import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import tornadofx.vbox
import tornadofx.vgrow

class AshesConsoleView: AshesBaseView() {
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
                println(line)
                console.appendText("redis:> ")
            }
        }
        return console
    }
}
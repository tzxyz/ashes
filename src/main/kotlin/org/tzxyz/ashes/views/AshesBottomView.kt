package org.tzxyz.ashes.views

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class AshesBottomView: AshesBaseView() {

    private val logPane = stackpane {
        id = "logPane"
        textarea {}
    }

    private val clicked = SimpleBooleanProperty(false)

    override val root = vbox {
        hgrow = Priority.ALWAYS
        style {
            padding = box(2.px)
        }
        stackpane {
            alignment = Pos.BASELINE_LEFT
            button {
                text = "History"
                style {
                    fontSize = 12.px
                    padding = box(5.px, 15.px, 5.px, 15.px)
                }
            }.action {
                clicked.set(clicked.get().not())
            }
        }
    }

    init {
        clicked.addListener { _, oldValue, _ ->
            if (!oldValue) {
                root.children.add(0, logPane)
            } else {
                root.children.remove(logPane)
            }
        }
    }
}
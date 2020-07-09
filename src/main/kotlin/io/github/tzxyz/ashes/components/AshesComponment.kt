package io.github.tzxyz.ashes.components

import io.github.tzxyz.ashes.models.AshesConnection
import javafx.event.EventTarget
import javafx.scene.control.ContextMenu
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import tornadofx.action
import tornadofx.attachTo
import tornadofx.item

fun EventTarget.console(connection: AshesConnection? = null, op: AshesConsole.() -> Unit = {}) = AshesConsole().attachTo(this, op) {
    if (connection != null) it.connection = connection
}

fun TabPane.keyTab(text: String, op: Tab.() -> Unit = {}): Tab = Tab().also {
    val tab = Tab()
    tab.text = text
    tab.contextMenu = ContextMenu().also {
        it.item("Close").action {
            tabs.remove(selectionModel.selectedItem)
        }
        it.item("Close Left").action {
            val currentIndex = tabs.indexOf(selectionModel.selectedItem)
            val markedTabs = mutableListOf<Tab>()
            tabs.forEachIndexed { index, t ->
                if (index < currentIndex) {
                    markedTabs.add(t)
                }
            }
            tabs.removeAll(markedTabs)
        }
        it.item("Close Right").action {
            val currentIndex = tabs.indexOf(selectionModel.selectedItem)
            val markedTabs = mutableListOf<Tab>()
            tabs.forEachIndexed { index, t ->
                if (index > currentIndex) {
                    markedTabs.add(t)
                }
            }
            tabs.removeAll(markedTabs)
        }
        it.item("Close Other").action {
            val markedTabs = mutableListOf<Tab>()
            for (t in tabs) {
                if (t !== selectionModel.selectedItem)
                    markedTabs.add(t)

            }
            tabs.removeAll(markedTabs)
        }
        it.item("Close All").action { tabs.removeAll { true } }
    }
    tabs.add(tab)
    return tab.also(op)
}

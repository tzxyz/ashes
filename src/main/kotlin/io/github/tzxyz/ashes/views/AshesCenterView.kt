package io.github.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import io.github.tzxyz.ashes.constants.KEY_CONSTANTS_SERVER_INFO
import io.github.tzxyz.ashes.constants.OPEN_CONSOLE_VIEW
import io.github.tzxyz.ashes.controllers.AshesKeyController
import io.github.tzxyz.ashes.events.AshesOpenKeyViewEvent
import io.github.tzxyz.ashes.models.*
import javafx.scene.control.Tab
import javafx.scene.layout.Priority
import tornadofx.*

class AshesCenterView : AshesBaseView() {

    private val keyController by inject<AshesKeyController>()

    override val root = vbox {
        tabpane {
            id = "keyTabs"
            vgrow = Priority.ALWAYS
            tabMaxWidth = 100.0
            tabMinWidth = 100.0
            subscribe<AshesOpenKeyViewEvent> { e ->
                when(e.key) {
                    KEY_CONSTANTS_SERVER_INFO -> {
                        val tab = tab("ServerInfo") {
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.INFORMATION, "1.4em"))
                            add(AshesServerInfoView(keyController.info()))
                        }
                        selectionModel.select(tab)
                    }
                    OPEN_CONSOLE_VIEW -> {
                        val tab = tab("Console") {
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CONSOLE, "1.4em"))
                            add(AshesConsoleView(e.connection))
                        }
                        selectionModel.select(tab)
                    }
                    else -> {
                        val keyValue = keyController.getKeyAndValue(e.key)
                        val tab = tab(e.key) {
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CODE_STRING, "1.4em"))
                            contextMenu = contextmenu {
                                item("Close").action {
                                    tabs.remove(selectionModel.selectedItem)
                                }
                                item("Close Left").action {
                                    val currentIndex = tabs.indexOf(selectionModel.selectedItem)
                                    val markedTabs = mutableListOf<Tab>()
                                    tabs.forEachIndexed { index, tab ->
                                        if (index < currentIndex) {
                                            markedTabs.add(tab)
                                        }
                                    }
                                    tabs.removeAll(markedTabs)
                                }
                                item("Close Right").action {
                                    val currentIndex = tabs.indexOf(selectionModel.selectedItem)
                                    val markedTabs = mutableListOf<Tab>()
                                    tabs.forEachIndexed { index, tab ->
                                        if (index > currentIndex) {
                                            markedTabs.add(tab)
                                        }
                                    }
                                    tabs.removeAll(markedTabs)
                                }
                                item("Close Other").action {
                                    val markedTabs = mutableListOf<Tab>()
                                    for (tab in tabs) {
                                        if (tab !== selectionModel.selectedItem)
                                            markedTabs.add(tab)

                                    }
                                    tabs.removeAll(markedTabs)
                                }
                                item("Close All").action { tabs.removeAll { true } }
                            }
                            when(keyValue) {
                                is AshesKeyStringValue -> add(AshesStringKeyView(keyValue))
                                is AshesKeyHashValue -> add(AshesHashKeyView(keyValue))
                                is AshesKeyListValue -> add(AshesListKeyView(keyValue))
                                is AshesKeySetValue -> add(AshesSetKeyView(keyValue))
                                is AshesKeySortedSetValue -> add(AshesSortedSetKeyView(keyValue))
                                else -> throw RuntimeException("Unknown Exception.")
                            }
                        }
                        selectionModel.select(tab)
                    }
                }
            }
        }
    }
}

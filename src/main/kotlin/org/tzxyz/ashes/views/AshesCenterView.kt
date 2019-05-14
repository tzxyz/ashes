package org.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.scene.control.Tab
import javafx.scene.layout.Priority
import org.tzxyz.ashes.controllers.AshesKeyController
import org.tzxyz.ashes.events.AshesOpenKeyViewEvent
import org.tzxyz.ashes.models.*
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
                val keyValue = keyController.getKeyAndValue(e.key)
                val viewClass = when(keyValue) {
                    is AshesKeyAndStringValue -> AshesStringKeyView::class
                    is AshesKeyAndHashValue -> AshesHashKeyView::class
                    is AshesKeyAndListValue -> AshesListKeyView::class
                    is AshesKeyAndSetValue -> AshesSetKeyView::class
                    is AshesKeyAndSortedSetValue -> AshesSortedSetKeyView::class
                    else -> throw RuntimeException("Unknown Exception.")
                }
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
                    add(find(viewClass))
                }
                selectionModel.select(tab)
            }
        }
    }
}

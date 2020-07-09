package io.github.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import io.github.tzxyz.ashes.components.keyTab
import io.github.tzxyz.ashes.constants.KEY_CONSTANTS_SERVER_INFO
import io.github.tzxyz.ashes.constants.OPEN_CONSOLE_VIEW
import io.github.tzxyz.ashes.controllers.AshesKeyController
import io.github.tzxyz.ashes.events.AshesOpenKeyViewEvent
import io.github.tzxyz.ashes.models.*
import javafx.scene.layout.Priority
import tornadofx.tabpane
import tornadofx.vbox
import tornadofx.vgrow

class AshesCenterView : AshesBaseView() {

    private val keyController by inject<AshesKeyController>()

    override val root = vbox {
        tabpane {
            id = "keyTabs"
            vgrow = Priority.ALWAYS
            tabMaxWidth = 100.0
            tabMinWidth = 100.0
            subscribe<AshesOpenKeyViewEvent> { e ->
                val tabId = "${e.connection?.id}-${e.key}"
                val tab = tabs.filter { it.id == tabId }.firstOrNull()
                if (tab != null) {
                    selectionModel.select(tab)
                    return@subscribe
                }
                when (e.key) {
                    KEY_CONSTANTS_SERVER_INFO -> {
                        keyTab("ServerInfo") {
                            id = tabId
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.INFORMATION, "1.4em"))
                            add(AshesServerInfoView(keyController.info()))
                        }
                    }
                    OPEN_CONSOLE_VIEW -> {
                        keyTab("Console") {
                            id = tabId
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CONSOLE, "1.4em"))
                            add(AshesConsoleView(e.connection))
                        }
                    }
                    else -> {
                        val keyValue = keyController.getKeyAndValue(e.key)
                        keyTab(e.key) {
                            id = tabId
                            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CODE_STRING, "1.4em"))
                            when (keyValue) {
                                is AshesKeyStringValue -> add(AshesStringKeyView(keyValue))
                                is AshesKeyHashValue -> add(AshesHashKeyView(keyValue))
                                is AshesKeyListValue -> add(AshesListKeyView(keyValue))
                                is AshesKeySetValue -> add(AshesSetKeyView(keyValue))
                                is AshesKeySortedSetValue -> add(AshesSortedSetKeyView(keyValue))
                                else -> throw RuntimeException("Unknown Exception.")
                            }
                        }
                    }
                }.also {
                    selectionModel.select(it)
                }
            }
        }
    }
}

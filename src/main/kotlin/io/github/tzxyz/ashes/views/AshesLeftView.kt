package io.github.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import io.github.tzxyz.ashes.constants.KEY_CONSTANTS_SERVER_INFO
import io.github.tzxyz.ashes.controllers.AshesConnectionController
import io.github.tzxyz.ashes.events.AshesNewConnectionEvent
import io.github.tzxyz.ashes.events.AshesOpenKeyViewEvent
import io.github.tzxyz.ashes.events.AshesScanKeyEvent
import io.github.tzxyz.ashes.events.AshesUpdateConnectionEvent
import io.github.tzxyz.ashes.fragments.AshesEditConnectionFragment
import io.github.tzxyz.ashes.fragments.AshesNewKeyFragment
import io.github.tzxyz.ashes.global.Current
import io.github.tzxyz.ashes.models.AshesConnection
import javafx.scene.control.TreeItem
import tornadofx.*
import java.util.concurrent.ConcurrentHashMap

class AshesLeftView : View() {

    private val connectionController by inject<AshesConnectionController>()

    private val loadedConnections = ConcurrentHashMap<AshesConnection, Boolean>()

    override val root = treeview<Any> {
        id = "leftView"
        root = TreeItem<Any>()
        isShowRoot = false
        cellFormat {
            when(it) {
                is AshesConnection -> {
                    var connected = false
                    text = it.name
                    graphic = connectionIcon()
                    contextMenu = contextMenu(it)
                    setOnMouseClicked { e ->
                        if (e.clickCount == 2) {
                            connected = true
                            if (!loadedConnections.containsKey(selectedValue!!)) {
                                connectionController.connect(selectedValue!! as AshesConnection)
                                selectionModel.selectedItem.graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.LAN_CONNECT, "1.4em"))
                            }
                        }
                        Current.setConnection(it)
                    }
                }
                is String -> {
                    text = it
                    addClass("ashes-key")
                    graphic = keyIcon()
                    contextMenu = null
                    setOnMouseClicked { e ->
                        if (e.clickCount == 2) {
                            fire(AshesOpenKeyViewEvent(selectedValue!! as String))
                        }
                    }
                }
            }
        }
        populate { parent ->
            if (parent == root) connectionController.load()
            else null
        }
        subscribe<AshesNewConnectionEvent> { e ->
            root.children.add(TreeItem<Any>(e.connection))
        }
        subscribe<AshesUpdateConnectionEvent> { e ->
            root.children.forEach { item ->
                if (e.before.id == (item.value as AshesConnection).id) {
                    item.valueProperty().set(e.connection)
                }
            }
        }
        subscribe<AshesScanKeyEvent> { e ->
            runAsync {
                root.children.find { it.value.equals(e.connection) }.let {
                    if (!loadedConnections.containsKey(e.connection)) {
                        e.keys.forEach { key ->
                            it?.children?.add(TreeItem(key))
                        }
                        loadedConnections[e.connection] = true
                        it?.expandAll()
                    }
                }
            }
        }
    }

    private fun connectionIcon(): JFXRippler {
        val icon = MaterialDesignIconView(MaterialDesignIcon.LAPTOP_MAC, "1.6em")
        icon.addClass("ashes-connection-icon")
        return JFXRippler(icon)
    }

    private fun keyIcon(): JFXRippler {
        val icon = MaterialDesignIconView(MaterialDesignIcon.KEY, "1.4em")
        icon.addClass("ashes-key-icon")
        return JFXRippler(icon)
    }

    private fun contextMenu(connection: AshesConnection) = contextmenu {
        item("Edit Connection") {
            graphic = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.TABLE_EDIT, "1.4em"))
            action {
                find<AshesEditConnectionFragment>(AshesEditConnectionFragment::current to connection).openModal()
            }
        }
        item("Reload")
        item("New Key").action {
            find<AshesNewKeyFragment>().openModal()
        }
        item("Open Console")
        item("Server Info").action { fire(AshesOpenKeyViewEvent(KEY_CONSTANTS_SERVER_INFO)) }
        item("Flush DB")
    }

}

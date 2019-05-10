package org.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.scene.control.TreeItem
import org.tzxyz.ashes.controllers.AshesConnectionController
import org.tzxyz.ashes.events.AshesNewConnectionEvent
import org.tzxyz.ashes.events.AshesOpenKeyViewEvent
import org.tzxyz.ashes.events.AshesScanKeyEvent
import org.tzxyz.ashes.events.AshesUpdateConnectionEvent
import org.tzxyz.ashes.fragments.AshesEditConnectionFragment
import org.tzxyz.ashes.fragments.AshesNewConnectionFragment
import org.tzxyz.ashes.global.Current
import org.tzxyz.ashes.models.AshesConnection
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
                    subscribe<AshesUpdateConnectionEvent> { e ->
                        if (e.before.id == it.id) {
                            text = e.connection.name
                        }
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
            if (parent == root) connectionController.loadConnections()
            else null
        }
        subscribe<AshesNewConnectionEvent> { e ->
            root.children.add(TreeItem<Any>(e.connection))
        }
        subscribe<AshesScanKeyEvent> { e ->
            runAsync {
                root.children.find { it.value.equals(e.connection) }.let { it ->
                    if (!loadedConnections.containsKey(e.connection)) {
                        e.keys.forEach { key ->
                            it?.children?.add(TreeItem(key))
                        }
                        loadedConnections.put(e.connection, true)
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
                find<AshesNewConnectionFragment>(AshesEditConnectionFragment::current to connection).openModal()
            }
        }
        item("New Key")
        item("Open Console")
        item("Server Info")
        item("Flush All")
    }

}

package io.github.tzxyz.ashes.components

import io.github.tzxyz.ashes.models.AshesConnection
import javafx.event.EventTarget
import tornadofx.attachTo

fun EventTarget.console(connection: AshesConnection? = null, op: AshesConsole.() -> Unit = {}) = AshesConsole().attachTo(this, op) {
    if (connection != null) it.connection = connection
}

package io.github.tzxyz.ashes.views

import io.github.tzxyz.ashes.components.console
import io.github.tzxyz.ashes.models.AshesConnection
import io.github.tzxyz.ashes.services.AshesConsoleService
import tornadofx.vbox

class AshesConsoleView(val connection: AshesConnection?): AshesBaseView() {

    private val consoleService by inject<AshesConsoleService>()

    override val root = vbox {
        console(connection) {
            service = consoleService
            init()
        }
    }

}

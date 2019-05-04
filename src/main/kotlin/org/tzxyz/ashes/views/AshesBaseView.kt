package org.tzxyz.ashes.views

import tornadofx.View
import java.nio.file.Path

abstract class AshesBaseView: View() {

    override val configPath: Path get()  = app.configBasePath.resolve("ashes.properties")

}
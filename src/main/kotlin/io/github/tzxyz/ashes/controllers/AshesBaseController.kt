package io.github.tzxyz.ashes.controllers

import tornadofx.Controller
import java.nio.file.Path

abstract class AshesBaseController: Controller() {
    override val configPath: Path get() = app.configBasePath.resolve("ashes.properties")
}
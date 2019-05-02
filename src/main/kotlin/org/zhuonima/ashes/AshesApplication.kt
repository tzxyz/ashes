package org.zhuonima.ashes

import javafx.scene.Scene
import org.zhuonima.ashes.views.HomeView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch

class AshesApplication: App(HomeView::class) {
    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root, 900.0, 600.0)
    }
}

fun main(args: Array<String>) {
    launch<AshesApplication>(args)
}
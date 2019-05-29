package io.github.tzxyz.ashes

import io.github.tzxyz.ashes.views.AshesHomeView
import javafx.scene.Scene
import tornadofx.*

class AshesApplication: App(AshesHomeView::class) {

    init {
        reloadStylesheetsOnFocus()
        reloadViewsOnFocus()
    }

    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root, 900.0, 600.0)
    }
}

fun main(args: Array<String>) {
    launch<AshesApplication>(args)
}
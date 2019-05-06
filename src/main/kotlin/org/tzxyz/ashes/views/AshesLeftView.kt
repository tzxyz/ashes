package org.tzxyz.ashes.views

import javafx.scene.control.TreeItem
import tornadofx.View
import tornadofx.treeview

class AshesLeftView : View() {

    override val root = treeview<Any> {
        root = TreeItem<Any>()
        isShowRoot = false
    }

}

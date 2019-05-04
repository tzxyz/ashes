package org.tzxyz.ashes.views

import tornadofx.borderpane

class AshesHomeView: AshesBaseView() {

    override val root = borderpane {
        top(AshesTopView::class)
    }

}
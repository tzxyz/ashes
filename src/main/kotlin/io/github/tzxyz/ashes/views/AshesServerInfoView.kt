package io.github.tzxyz.ashes.views

import tornadofx.textarea

class AshesServerInfoView(private val info: String): AshesBaseView() {

    override val root = textarea(this.info) {
        isEditable = false
    }
}
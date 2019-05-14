package org.tzxyz.ashes.views

import tornadofx.vbox
import tornadofx.text

class AshesStringKeyView: AshesBaseView() {
    override val root = vbox {
        text("ashes string")
        text("ashes string")
        text("ashes string")
    }
}

class AshesListKeyView: AshesBaseView() {
    override val root = vbox {
        text("ashes list")
        text("ashes list")
        text("ashes list")
    }
}

class AshesHashKeyView: AshesBaseView() {
    override val root = vbox {
        text("ashes hash")
        text("ashes hash")
        text("ashes hash")
    }
}

class AshesSetKeyView: AshesBaseView() {
    override val root = vbox {
        text("ashes set")
        text("ashes set")
        text("ashes set")
    }
}

class AshesSortedSetKeyView: AshesBaseView() {
    override val root = vbox {
        text("ashes sorted set")
        text("ashes sorted set")
        text("ashes sorted set")
    }
}
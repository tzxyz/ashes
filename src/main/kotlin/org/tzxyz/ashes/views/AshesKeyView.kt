package org.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import org.tzxyz.ashes.models.*
import org.tzxyz.ashes.viewmodels.AshesKeyAndValueViewModel
import tornadofx.*

abstract class AshesBaseKeyView constructor(open val keyAndValue: AshesKeyAndValue): AshesBaseView() {

    private fun command(): StyleClassedTextArea {
        val command = StyleClassedTextArea()
        command.paragraphGraphicFactory = LineNumberFactory.get(command)
        command.isWrapText = true
        command.minHeight = 100.0
        command.maxHeight = 100.0
        return command
    }

    fun buildView() = vbox {
        vgrow = Priority.ALWAYS
        hbox {
            spacing = 8.0

            style {
                padding = box(2.px, 5.px, 5.px, 2.px)
            }

            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CAST_CONNECTED, "1.6em")))
                text {
                    text = "(%s:%s)".format("127.0.0.1", "6379")
                    alignment = Pos.CENTER
                    font = Font.font(12.0)
                }
            }

            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.DATABASE, "1.6em")))
                text {
                    text = "%s".format("0")
                    alignment = Pos.CENTER
                    font = Font.font(12.0)
                }
            }

            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.TIMER_SAND, "1.5em")))
                text {
                    text = "%ss".format(keyAndValue.ttl)
                    alignment = Pos.CENTER
                    font = Font.font(12.0)
                }
            }
        }
        add(command())
        hbox {
            spacing = 8.0
            padding = insets(5.0)
            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CODE_STRING, "1.4em")))
                text {
                    text = "%s".format(keyAndValue.key)
                    font = Font.font(12.0)
                    alignment = Pos.CENTER
                }
            }
            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.TIMER, "1.4em")))
                text {
                    System.currentTimeMillis()
                    text = "%s millisecond".format(keyAndValue.cost)
                    font = Font.font(12.0)
                    alignment = Pos.CENTER
                }
            }
            region { hgrow = Priority.ALWAYS }
            add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.FILE_TREE, "1.4em")))
            add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.TABLE, "1.4em")))
            add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT, "1.4em")))
        }
        add(contentView())
    }

    abstract fun contentView(): Parent
}

class AshesStringKeyView(override val keyAndValue: AshesKeyAndStringValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        text("ashes string")
        text("ashes string")
        text("ashes string")
    }
}

class AshesListKeyView: View() {

//    private val viewmodel by inject<AshesKeyAndValueViewModel>()

//    val current by param<AshesKeyAndListValue>()
//
//    init {
//        viewmodel.rebind { itemProperty.set(current) }
//    }

//    override val titleProperty = SimpleStringProperty("ashes")


    override val root = vbox {
        text("ashes list")
        text("ashes list")
        text("ashes list")
    }
}

class AshesHashKeyView(override val keyAndValue: AshesKeyAndHashValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        text("ashes hash")
        text("ashes hash")
        text("ashes hash")
    }
}

class AshesSetKeyView(override val keyAndValue: AshesKeyAndSetValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        text("ashes set")
        text("ashes set")
        text("ashes set")
    }
}

class AshesSortedSetKeyView(override val keyAndValue: AshesKeyAndSortedSetValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        text("ashes sorted set")
        text("ashes sorted set")
        text("ashes sorted set")
    }
}
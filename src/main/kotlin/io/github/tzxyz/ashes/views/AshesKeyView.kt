package io.github.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import io.github.tzxyz.ashes.controllers.AshesKeyController
import io.github.tzxyz.ashes.models.*
import io.github.tzxyz.ashes.viewmodels.AshesListValueViewModel
import io.github.tzxyz.ashes.viewmodels.AshesStringValueViewModel
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TableRow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.util.Duration
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import tornadofx.*
import tornadofx.controlsfx.customNotification

abstract class AshesBaseKeyView constructor(open val keyAndValue: AshesKeyValue): AshesBaseView() {

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

class AshesStringKeyView: AshesBaseView() {

    private val keyController by inject<AshesKeyController>()

    private val viewModel by inject<AshesStringValueViewModel>(Scope())

    val key by param<String>()

    var keyAndValue: AshesKeyStringValue

    init {
        keyAndValue = keyController.getKeyAndValue(key) as AshesKeyStringValue
        viewModel.rebind { itemProperty.set(keyAndValue) }
    }

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
            val saveIcon = JFXRippler(MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE, "1.4em"))
            saveIcon.onMouseClickedProperty().set(object : EventHandler<MouseEvent> {
                override fun handle(event: MouseEvent?) {
                    viewModel.commit {
                        viewModel.rebind { itemProperty.set(keyController.set(viewModel.key.value, viewModel.value.value) as AshesKeyStringValue) }
                        customNotification(
                            title = "Success",
                            graphic = MaterialDesignIconView(MaterialDesignIcon.SCALE),
                            text = "Update Value Success.",
                            hideAfter = Duration.seconds(3.0),
                            position = Pos.CENTER
                        )
                    }
                }
            })
            add(saveIcon)
        }
        add(contentView())
    }

    override val root = buildView()

    private fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        add(stringValueTextArea())
    }

    private fun stringValueTextArea(): StyleClassedTextArea {
        val valueTextArea = CodeArea()
        valueTextArea.id = "string-value"
        valueTextArea.replaceText(keyAndValue.value)
        valueTextArea.isWrapText = true
        valueTextArea.isEditable = true
        valueTextArea.isWrapText = true
        valueTextArea.vgrow = Priority.ALWAYS
        valueTextArea.paragraphGraphicFactory = LineNumberFactory.get(valueTextArea)
        valueTextArea.textProperty().addListener { _, _, newValue ->
            viewModel.value.value = newValue
        }
        return valueTextArea
    }

}

class AshesListKeyView(override val keyAndValue: AshesKeyListValue): AshesBaseKeyView(keyAndValue) {

    private val keyController by inject<AshesKeyController>()

    private val viewModel by inject<AshesListValueViewModel>(Scope())

    init {
        viewModel.itemProperty.set(keyAndValue)
    }

    override val root = buildView()

    override fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        tableview(keyAndValue.value.mapIndexed { index, s -> index to s }.asObservable()) {
            vgrow = Priority.ALWAYS
            readonlyColumn("#", Pair<Int, String>::first)
            readonlyColumn("value", Pair<Int, String>::second)
            columnResizePolicy = SmartResize.POLICY
            setRowFactory {
                val row = TableRow<Pair<Int, String>>()
                row.setOnMouseClicked { e ->
                    if (e.clickCount == 2) {
                        println(row.item)
                    }
                }
                row
            }
        }
    }
}

class AshesHashKeyView(override val keyAndValue: AshesKeyHashValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        tableview(keyAndValue.value) {
            vgrow = Priority.ALWAYS
            readonlyColumn("key", Pair<String, String>::first)
            readonlyColumn("value", Pair<String, String>::second)
            columnResizePolicy = SmartResize.POLICY
            setRowFactory {
                val row = TableRow<Pair<String, String>>()
                row.setOnMouseClicked { e ->
                    if (e.clickCount == 2) {
                        println(row.item)
                    }
                }
                row
            }
        }
    }
}

class AshesSetKeyView(override val keyAndValue: AshesKeySetValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        tableview(keyAndValue.value.mapIndexed { index, s -> index to s }.asObservable()) {
            vgrow = Priority.ALWAYS
            readonlyColumn("#", Pair<Int, String>::first)
            readonlyColumn("value", Pair<Int, String>::second)
            columnResizePolicy = SmartResize.POLICY
            setRowFactory {
                val row = TableRow<Pair<Int, String>>()
                row.setOnMouseClicked { e ->
                    if (e.clickCount == 2) {
                        println(row.item)
                    }
                }
                row
            }
        }
    }
}

class AshesSortedSetKeyView(override val keyAndValue: AshesKeySortedSetValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        tableview(keyAndValue.value) {
            vgrow = Priority.ALWAYS
            readonlyColumn("score", Pair<Double, String>::first)
            readonlyColumn("value", Pair<Double, String>::second)
            columnResizePolicy = SmartResize.POLICY
            setRowFactory {
                val row = TableRow<Pair<Double, String>>()
                row.setOnMouseClicked { e ->
                    if (e.clickCount == 2) {
                        println(row.item)
                    }
                }
                row
            }
        }
    }
}
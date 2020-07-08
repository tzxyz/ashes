package io.github.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import io.github.tzxyz.ashes.components.console
import io.github.tzxyz.ashes.models.*
import io.github.tzxyz.ashes.viewmodels.AshesListValueViewModel
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TableRow
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.StyleClassedTextArea
import tornadofx.*

abstract class AshesBaseKeyView constructor(open val keyAndValue: AshesKeyValue): AshesBaseView() {


    fun buildView() = vbox {
        vgrow = Priority.ALWAYS
        hbox {
            console {
                hgrow = Priority.ALWAYS
                paragraphGraphicFactory = LineNumberFactory.get(this)
                isWrapText = true
                minHeight = 100.0
                maxHeight = 100.0
            }
        }
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
                    text = "%s milliseconds".format(keyAndValue.cost)
                    font = Font.font(12.0)
                    alignment = Pos.CENTER
                }
            }
            hbox {
                spacing = 2.0
                add(JFXRippler(MaterialDesignIconView(MaterialDesignIcon.TIMER_SAND, "1.4em")))
                text {
                    text = if (keyAndValue.ttl == -1L) "∞" else "%ss".format(keyAndValue.ttl)
                    alignment = Pos.CENTER
                    font = Font.font(12.0)
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

class AshesStringKeyView(override val keyAndValue: AshesKeyStringValue): AshesBaseKeyView(keyAndValue) {

    override val root = buildView()

    override fun contentView() = vbox {
        vgrow = Priority.ALWAYS
        add(stringValueTextArea())
    }

    private fun stringValueTextArea(): StyleClassedTextArea {
        val valueTextArea = StyleClassedTextArea()
        valueTextArea.id = "string-value"
        valueTextArea.replaceText(keyAndValue.value)
        valueTextArea.isWrapText = true
        valueTextArea.isEditable = true
        valueTextArea.isDisable = true
        valueTextArea.vgrow = Priority.ALWAYS
        valueTextArea.paragraphGraphicFactory = LineNumberFactory.get(valueTextArea)
        return valueTextArea
    }

}

class AshesListKeyView(override val keyAndValue: AshesKeyListValue): AshesBaseKeyView(keyAndValue) {

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

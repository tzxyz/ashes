package io.github.tzxyz.ashes.fragments

import io.github.tzxyz.ashes.constants.*
import io.github.tzxyz.ashes.controllers.AshesKeyController
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.layout.Priority
import tornadofx.*
import java.lang.RuntimeException

class AshesNewKeyFragment: AshesBaseFragment() {

    private val newKey = AshesNewKey()

    private val keyController by inject<AshesKeyController>()

    override val root = vbox {
        title = "New Key"
        prefWidth = 600.0
        prefHeight = 400.0
        form {
            vgrow = Priority.ALWAYS
            fieldset {
                vgrow = Priority.ALWAYS
                field("Key : ") {
                    textfield().bind(newKey.key)
                }
                field("Type : ") {
                    combobox(values = listOf(STRING, LIST, SET, ZSET, HASH)) {
                        id = "key-type"
                        hgrow = Priority.ALWAYS
                        bind(newKey.type)
                        selectionModel.selectFirst()
                        valueProperty().addListener { observable, oldValue, newValue ->
                            val node = this@form.lookup("#new-key-value-view")
                            this@fieldset.children.remove(node)
                            when(newValue) {
                                STRING -> {
                                    this@fieldset.children.add(2, field("Value : ") {
                                        id = "new-key-value-view"
                                        textarea {  }
                                    })
                                }
                                LIST -> {
                                    this@fieldset.children.add(2, field("Value : ") {
                                        id = "new-key-value-view"
                                        val values = listOf("New Value Here.").asObservable()
                                        listview(values) {
                                            isEditable = true
                                            cellFactory = TextFieldListCell.forListView()
                                        }
                                    })
                                }
                                else -> {

                                }
                            }
                        }
                    }
                }
                field("Value : ")  {
                    id = "new-key-value-view"
                    vgrow = Priority.ALWAYS
                    val type = (this@form.lookup("#key-type") as ComboBox<String>).selectedItem
                    when(type) {
                        "string" -> {
                            textarea {
                                bind(newKey.stringValue)
                            }
                        }
                        else -> {

                        }
                    }
                }
                hbox {
                    alignment = Pos.BASELINE_RIGHT
                    spacing = 20.0
                    button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                    button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                        println(newKey)
                        when(newKey.type.value) {
                            STRING -> keyController.set(newKey.key.value, newKey.stringValue.value)
                            LIST -> {}
                            SET -> {}
                            ZSET -> {}
                            HASH -> {}
                            else -> throw RuntimeException("unknown redis key type")
                        }
                        close()
                    }
                }
            }
        }
    }
}

class AshesNewKey {
    val key = SimpleStringProperty()
    val type = SimpleStringProperty()
    val stringValue = SimpleStringProperty()
    val listValue = SimpleListProperty<String>()
    val setValue = SimpleSetProperty<String>()
    val zsetValue = SimpleListProperty<Pair<Double, String>>()
    val hashValue = SimpleListProperty<Pair<String, String>>()
}
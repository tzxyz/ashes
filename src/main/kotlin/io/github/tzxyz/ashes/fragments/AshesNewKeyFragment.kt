package io.github.tzxyz.ashes.fragments

import io.github.tzxyz.ashes.constants.*
import io.github.tzxyz.ashes.controllers.AshesKeyController
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.util.concurrent.atomic.AtomicInteger


class AshesNewKeyFragment: AshesBaseFragment() {

    private val newKey = AshesNewKey()

    private val keyController by inject<AshesKeyController>()

    override val root = vbox {
        title = "New Key"
        val context = ValidationContext()
        form {
            vgrow = Priority.ALWAYS
            fieldset("Key:") {
                field {
                    combobox(values = listOf(STRING, LIST, SET, ZSET, HASH)) {
                        id = "key-type"
                        hgrow = Priority.ALWAYS
                        bind(newKey.type)
                        selectionModel.selectFirst()
                        valueProperty().addListener { _, _, newValue ->
                            val node = this@form.lookup("#new-key-value-view")
                            val container = this@form.lookup("#new-key-value-container")
                            this@fieldset.children.remove(node)
                            container.getChildList()?.remove(node)
                            when(newValue) {
                                STRING -> {
                                    container.addChildIfPossible(field {
                                        id = "new-key-value-view"
                                        textarea {
                                            prefHeight = 300.0
                                            bind(newKey.stringValue)
                                            context.addValidator(this, this.textProperty()) {
                                                if (it.isNullOrBlank()) error("The value is required.") else null
                                            }
                                        }
                                    }, 1)
                                }
                                LIST -> {
                                    container.addChildIfPossible(field {
                                        id = "new-key-value-view"
                                        val values = FXCollections.observableArrayList<AshesNewListValue>()
                                        newKey.listValue.value = values
                                        vbox {
                                            hbox {
                                                button("+").action {
                                                    (this@vbox.lookup("#listTableView") as TableView<AshesNewListValue>).items.add(AshesNewListValue(1, "double click here.", false))
                                                }
                                                button("-")

                                            }
                                            tableview(newKey.listValue) {
                                                id = "listTableView"
                                                prefHeight = 300.0
                                                columnResizePolicy = SmartResize.POLICY
//                                            column("idx", AshesNewListValue::idx)
                                                column("value", AshesNewListValue::value)
                                                column("operator", AshesNewListValue::op).cellFormat {
//                                                    button("+").action { items.add(AshesNewListValue(1, "double click here.", false)) }
                                                    graphic = hbox(spacing = 5) {
                                                        button("-").action { items.remove(this@cellFormat.rowItem) }
                                                    }
//
                                                }
//                                                addEventFilter(MouseEvent.MOUSE_CLICKED, EventHandler { e ->
//                                                    if (e.clickCount == 2) {
//                                                        val pos = focusModel.focusedCell
//                                                        if (pos.row == items.size - 1) {
//                                                            val listValue = AshesNewListValue(1, "double click here.", false)
//                                                            items.add(listValue)
//                                                        }
//                                                    }
//                                                })
                                                context.addValidator(this, this.itemsProperty()) {
                                                    if (it.isNullOrEmpty()) error("The value is required.")
                                                    else null
                                                }
                                            }
                                        }

                                    }, 1)
//                                    container.addChildIfPossible(field {
//                                        id = "new-key-value-view"
//                                        add(AshesListKeyContainer(newKey.listValue))
//                                    }, 1)
                                }
                                SET -> {
                                    container.addChildIfPossible(field {
                                        id = "new-key-value-view"
                                        val values = FXCollections.observableArrayList<String>("New Value Here.")
                                        newKey.setValue.value = values
                                        listview(newKey.setValue) {
                                            isEditable = true
                                            prefHeight = 300.0
                                            cellFactory = TextFieldListCell.forListView()
                                        }
                                    }, 1)
                                }
                                ZSET -> {
                                    container.addChildIfPossible(field {
                                        id = "new-key-value-view"
                                        val values = FXCollections.observableArrayList<String>("New Value Here.")
                                        newKey.setValue.value = values
                                        listview(newKey.setValue) {
                                            isEditable = true
                                            prefHeight = 300.0
                                            cellFactory = TextFieldListCell.forListView()
                                        }
                                    }, 1)
                                }
                                HASH -> {
                                    container.addChildIfPossible(field {
                                        id = "new-key-value-view"
                                        val values = FXCollections.observableArrayList<AshesNewHashValue>()
                                        values.add(AshesNewHashValue("double click and new hash key here.", "double click and new hash value here."))
                                        newKey.hashValue.set(values)
                                        tableview(values) {
                                            prefHeight = 300.0
                                            column("key", AshesNewHashValue::key).makeEditable()
                                            column("value", AshesNewHashValue::value).makeEditable()
                                            columnResizePolicy = SmartResize.POLICY
                                            addEventFilter(MouseEvent.MOUSE_CLICKED, EventHandler { e ->
                                                if (e.clickCount == 2) {
                                                    val pos = focusModel.focusedCell
                                                    if (pos.row == items.size - 1) {
                                                        val hashValue = AshesNewHashValue("double click and new hash key here.", "double click and new hash value here.")
                                                        items.add(hashValue)
                                                    }
                                                }
                                            })
                                        }
                                    }, 1)
                                }
                                else -> {
                                    throw UnsupportedOperationException("Unsupported Key Type.")
                                }
                            }
                        }
                    }
                    textfield {
                        bind(newKey.key)
                        context.addValidator(this, this.textProperty()) {
                            if (it.isNullOrBlank()) error("The Key is required.") else null
                        }
                    }
                }
            }
            fieldset("Value:") {
                id = "new-key-value-container"
                field  {
                    id = "new-key-value-view"
                    textarea {
                        prefHeight = 300.0
                    }.bind(newKey.stringValue)
                }
                field {
                    hbox {
                        alignment = Pos.BASELINE_RIGHT
                        spacing = 20.0
                        button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                        button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                            context.validators.forEach {
                                if (!it.validate()) return@action
                            }
                            when(newKey.type.value) {
                                STRING -> keyController.set(newKey.key.value, newKey.stringValue.value)
                                LIST -> keyController.lpush(newKey.key.value, newKey.listValue.value.map { it.value })
                                SET -> keyController.sadd(newKey.key.value, newKey.setValue.value.toSet())
                                ZSET -> keyController.zadd(newKey.key.value, newKey.setValue.value.toSet())
                                HASH -> keyController.hmset(newKey.key.value, newKey.hashValue.value.map { it.key to it.value }.toMap())
                                else -> throw RuntimeException("unknown redis key type")
                            }
                            close()
                        }
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
    val listValue = SimpleListProperty<AshesNewListValue>()
    val setValue = SimpleListProperty<String>()
    val zsetValue = SimpleListProperty<Pair<Double, String>>()
    val hashValue = SimpleListProperty<AshesNewHashValue>()
}

class AshesListKeyContainer(val list: SimpleListProperty<AshesNewListValue>): VBox() {
    init {
        children.addAll(AshesListKeyForm(this))
    }
}
class AshesListKeyForm(parent: Parent): HBox() {
    private val counter = AtomicInteger()
    private val textField = TextField("item")
    private val plusButton = Button("+")
    private val removeButton = Button("-")

    init {
        hgrow = Priority.ALWAYS
        this.children.addAll(textField, plusButton, removeButton)
        plusButton.action { parent.getChildList()?.add(AshesListKeyForm(parent)) }
        removeButton.action { parent.getChildList()?.let { if (it.size > 1) { it.remove(this) } } }
    }
}

data class AshesNewListValue(var idx: Int, var value: String, var op: Boolean)
data class AshesNewHashValue(var key: String, var value: String)
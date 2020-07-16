package io.github.tzxyz.ashes.fragments

import io.github.tzxyz.ashes.constants.*
import io.github.tzxyz.ashes.controllers.AshesKeyController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import tornadofx.*


class AshesNewKeyFragment: AshesBaseFragment() {

    private val newKey = AshesNewKey()

    private val keyController by inject<AshesKeyController>()

    override val root = vbox {
        title = "New Key"
        val context = ValidationContext()
        form {
            vgrow = Priority.ALWAYS
            fieldset("Key:") {
                hbox {
                    field {
                        hgrow = Priority.ALWAYS
                        combobox(values = listOf(STRING, HASH, LIST, SET, ZSET)) {
                            id = "key-type"
                            hgrow = Priority.ALWAYS
                            bind(newKey.type)
                            selectionModel.selectFirst()
                            valueProperty().addListener { _, _, newValue ->
                                val node = this@form.lookup("#new-key-value-view")
                                val bar = this@form.lookup("#new-key-value-operator-bar")
                                val container = this@form.lookup("#new-key-value-container")
                                container.getChildList()?.remove(node)
                                container.getChildList()?.remove(bar)
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
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-operator-bar"
                                            hbox {
                                                alignment = Pos.BASELINE_RIGHT
                                                spacing = 12.0
                                                button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                                                button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                                                    context.validators.forEach {
                                                        if (!it.validate()) return@action
                                                    }
                                                    keyController.set(newKey)
                                                    close()
                                                }
                                            }
                                        }, 2)
                                    }
                                    LIST -> {
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-view"
                                            val values = FXCollections.observableArrayList<AshesNewListValue>()
                                            newKey.listValue.value = values
                                            vbox {
                                                tableview(newKey.listValue) {
                                                    id = "listTableView"
                                                    prefHeight = 300.0
                                                    columnResizePolicy = SmartResize.POLICY
                                                    column("value", AshesNewListValue::value).makeEditable()
                                                    context.addValidator(this, this.itemsProperty()) {
                                                        if (it.isNullOrEmpty()) error("The value is required.")
                                                        else null
                                                    }
                                                }
                                            }

                                        }, 1)
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-operator-bar"
                                            hbox {
                                                alignment = Pos.BASELINE_RIGHT
                                                spacing = 12.0
                                                button("Add Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val listTableView = (this@vbox.lookup("#listTableView") as TableView<AshesNewListValue>)
                                                    val row = listTableView.items.size
                                                    listTableView.items.add(AshesNewListValue(""))
                                                    listTableView.layout()
                                                    listTableView.edit(row, listTableView.columns[0])

                                                }
                                                button("Remove Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val listTableView = (this@vbox.lookup("#listTableView") as TableView<AshesNewListValue>)
                                                    listTableView.selectedItem?.let { listTableView.items.remove(it) }
                                                }
                                                button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                                                button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                                                    context.validators.forEach {
                                                        if (!it.validate()) return@action
                                                    }
                                                    keyController.rpush(newKey.key.value, newKey.listValue.value.map { it.value })
                                                    close()
                                                }
                                            }
                                        }, 2)
                                    }
                                    SET -> {
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-view"
                                            newKey.setValue.value = FXCollections.observableArrayList<AshesNewSetValue>()
                                            tableview(newKey.setValue) {
                                                id = "setTableView"
                                                prefHeight = 300.0
                                                columnResizePolicy = SmartResize.POLICY
                                                column("value", AshesNewSetValue::value).makeEditable()
                                                context.addValidator(this, this.itemsProperty()) {
                                                    if (it.isNullOrEmpty()) error("The value is required.")
                                                    else null
                                                }
                                            }
                                        }, 1)
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-operator-bar"
                                            hbox {
                                                alignment = Pos.BASELINE_RIGHT
                                                spacing = 12.0
                                                button("Add Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val setTableView = (this@vbox.lookup("#setTableView") as TableView<AshesNewSetValue>)
                                                    val row = setTableView.items.size
                                                    setTableView.items.add(AshesNewSetValue(""))
                                                    setTableView.layout()
                                                    setTableView.edit(row, setTableView.columns[0])

                                                }
                                                button("Remove Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val setTableView = (this@vbox.lookup("#setTableView") as TableView<AshesNewSetValue>)
                                                    setTableView.selectedItem?.let { setTableView.items.remove(it) }
                                                }
                                                button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                                                button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                                                    context.validators.forEach {
                                                        if (!it.validate()) return@action
                                                    }
                                                    keyController.sadd(newKey.key.value, newKey.setValue.value.map { it.value }.toSet())
                                                    close()
                                                }
                                            }
                                        }, 2)
                                    }
                                    ZSET -> {
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-view"
                                            val values = FXCollections.observableArrayList<AshesNewZSetValue>()
                                            newKey.zsetValue.set(values)
                                            tableview(values) {
                                                id = "zSetTableView"
                                                prefHeight = 300.0
                                                column("score", AshesNewZSetValue::score).makeEditable()
                                                column("value", AshesNewZSetValue::value).makeEditable()
                                                columnResizePolicy = SmartResize.POLICY
                                                context.addValidator(this, this.itemsProperty()) {
                                                    if (it.isNullOrEmpty()) error("The value is required.")
                                                    else null
                                                }
                                            }
                                        }, 1)
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-operator-bar"
                                            hbox {
                                                alignment = Pos.BASELINE_RIGHT
                                                spacing = 12.0
                                                button("Add Item", svgicon(ADD_BUTTON, color = c("1296db"), size = 14)).action {
                                                    val zSetTableView = (this@vbox.lookup("#zSetTableView") as TableView<AshesNewZSetValue>)
                                                    val row = zSetTableView.items.size
                                                    zSetTableView.items.add(AshesNewZSetValue("", 0.0))
                                                    zSetTableView.layout()
                                                    zSetTableView.edit(row, zSetTableView.columns[0])
                                                }
                                                button("Remove Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val zSetTableView = (this@vbox.lookup("#zSetTableView") as TableView<AshesNewZSetValue>)
                                                    zSetTableView.selectedItem?.let { zSetTableView.items.remove(it) }
                                                }
                                                button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                                                button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                                                    context.validators.forEach {
                                                        if (!it.validate()) return@action
                                                    }
                                                    keyController.zadd(newKey.key.value, newKey.zsetValue.value.map { it.value to it.score }.toMap())
                                                    close()
                                                }
                                            }
                                        }, 2)
                                    }
                                    HASH -> {
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-view"
                                            val values = FXCollections.observableArrayList<AshesNewHashValue>()
                                            newKey.hashValue.set(values)
                                            tableview(values) {
                                                id = "hashTableView"
                                                prefHeight = 300.0
                                                column("key", AshesNewHashValue::key).makeEditable()
                                                column("value", AshesNewHashValue::value).makeEditable()
                                                columnResizePolicy = SmartResize.POLICY
                                                context.addValidator(this, this.itemsProperty()) {
                                                    if (it.isNullOrEmpty()) error("The value is required.")
                                                    else null
                                                }
                                            }
                                        }, 1)
                                        container.addChildIfPossible(field {
                                            id = "new-key-value-operator-bar"
                                            hbox {
                                                alignment = Pos.BASELINE_RIGHT
                                                spacing = 12.0
                                                button("Add Item", svgicon(ADD_BUTTON, color = c("1296db"), size = 14)).action {
                                                    val hashTableView = (this@vbox.lookup("#hashTableView") as TableView<AshesNewHashValue>)
                                                    val row = hashTableView.items.size
                                                    hashTableView.items.add(AshesNewHashValue("", ""))
                                                    hashTableView.layout()
                                                    hashTableView.edit(row, hashTableView.columns[0])
                                                }
                                                button("Remove Item", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action {
                                                    val listTableView = (this@vbox.lookup("#hashTableView") as TableView<AshesNewHashValue>)
                                                    listTableView.selectedItem?.let { listTableView.items.remove(it) }
                                                }
                                                button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                                                button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                                                    context.validators.forEach {
                                                        if (!it.validate()) return@action
                                                    }
                                                    keyController.hmset(newKey.key.value, newKey.hashValue.value.map { it.key to it.value }.toMap())
                                                    close()
                                                }
                                            }
                                        }, 2)
                                    }
                                    else -> {
                                        throw UnsupportedOperationException("Unsupported Key Type.")
                                    }
                                }
                            }
                        }
                        textfield {
                            bind(newKey.key)
                            hgrow = Priority.ALWAYS
                            context.addValidator(this, this.textProperty()) {
                                if (it.isNullOrBlank()) error("The Key is required.") else null
                            }
                        }
                    }

                    field {
                        padding = tornadofx.insets(5.0, 0.0, 5.0, 5.0)
                        text = "ttl"
                        textfield {
                            bind(newKey.ttl)
                            context.addValidator(this, this.textProperty()) {
                                if (it.isNullOrBlank()) error("The ttl is required.") else null
                                if (!it!!.isInt()) error("The ttl must be a number.") else null
                                try {
                                    if (it.toInt() != -1 && it.toInt() < 0) error("The ttl must be positive.") else null
                                } catch (ignore: NumberFormatException) {
                                    return@addValidator null
                                }
                            }
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
                    id = "new-key-value-operator-bar"
                    hbox {
                        alignment = Pos.BASELINE_RIGHT
                        spacing = 12.0
                        button("Cancel", SVGIcon(CANCEL_BUTTON, color = c("d81e06"), size = 14)).action{ close() }
                        button("Save", SVGIcon(SAVE_BUTTON, color = c("1296db"), size = 14)).action {
                            context.validators.forEach {
                                if (!it.validate()) return@action
                            }
                            keyController.set(newKey)
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
    val setValue = SimpleListProperty<AshesNewSetValue>()
    val zsetValue = SimpleListProperty<AshesNewZSetValue>()
    val hashValue = SimpleListProperty<AshesNewHashValue>()
    val ttl = SimpleIntegerProperty(-1)
}

data class AshesNewListValue(var value: String)
data class AshesNewSetValue(var value: String)
data class AshesNewZSetValue(var value: String, var score: Double)
data class AshesNewHashValue(var key: String, var value: String)

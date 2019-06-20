package io.github.tzxyz.ashes.fragments

import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.layout.Priority
import tornadofx.*

class AshesNewKeyFragment: AshesBaseFragment() {
    override val root = vbox {
        title = "New Key"
        prefWidth = 600.0
        prefHeight = 400.0
        form {
            vgrow = Priority.ALWAYS
            fieldset {
                vgrow = Priority.ALWAYS
                field("Key : ") {
                    textfield()
                }
                field("Type : ") {
                    combobox(values = listOf("String", "List", "Set", "ZSet", "Hash")) {
                        id = "key-type"
                        hgrow = Priority.ALWAYS
                        selectionModel.selectFirst()
                        valueProperty().addListener { observable, oldValue, newValue ->
                            val node = this@form.lookup("#new-key-value-view")
                            this@fieldset.children.remove(node)
                            when(newValue) {
                                "String" -> {
                                    this@fieldset.children.add(2, field("Value : ") {
                                        id = "new-key-value-view"
                                        textarea {  }
                                    })
                                }
                                "List" -> {
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
                        "String" -> {
                            textarea {
//                                prefWidthProperty().bind(this@field.widthProperty())
//                                prefHeightProperty().bind(this@field.heightProperty())
                            }
                        }
                        else -> {

                        }
                    }
                }
                hbox {
                    alignment = Pos.BASELINE_RIGHT
                    spacing = 20.0
                    button("Cancel", SVGIcon("M595.392 504.96l158.4 158.336-90.496 90.496-158.4-158.4-158.4 158.4L256 663.296l158.4-158.4L256 346.496 346.496 256 504.96 414.4 663.296 256l90.496 90.496L595.392 504.96zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832z", color = c("d81e06"), size = 14)).action{ close() }
                    button("Save", SVGIcon("M85.312 85.312v853.376h853.376v-512L597.312 85.312h-512zM0 0h640l384 384v640H0V0z m170.688 512v341.312h170.624V512H170.688z m256 0v341.312h170.624V512H426.688z m256 0v341.312h170.624V512h-170.624z m-512-341.312v256h680.192l-253.568-256H170.688z", color = c("1296db"), size = 14))
                }
            }
        }
    }
}
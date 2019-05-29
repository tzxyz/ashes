package io.github.tzxyz.ashes.fragments

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleListProperty
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import io.github.tzxyz.ashes.controllers.AshesConnectionController
import io.github.tzxyz.ashes.models.AshesConnection
import io.github.tzxyz.ashes.viewmodels.AshesEditConnectionItemViewModel
import io.github.tzxyz.ashes.viewmodels.AshesNewConnectionItemViewModel
import tornadofx.*
import java.util.*

class AshesNewConnectionFragment: AshesBaseFragment() {

    private val connectionController by inject<AshesConnectionController>()

    private val newConnectionViewModel by inject<AshesNewConnectionItemViewModel>()

    override val root = form {
        title = "New Connection"
        fieldset {
            field("Id") {
                textfield(newConnectionViewModel.id)
                hide()
            }
            field("Name") {
                textfield(newConnectionViewModel.name) {
                    required()
                }
            }
            field("Host") {
                textfield(newConnectionViewModel.host) {
                    required()
                }
            }
            hbox {
                alignment = Pos.BASELINE_LEFT
                spacing = 20.0
                field("Type") {
                    combobox(values = listOf("Singleton", "Shared")) {
                        selectionModel.selectFirst()
                    }
                }
                field("Port") {
                    spinner(editable = true, enableScroll = true, min = 0, max = 65535, property = newConnectionViewModel.port) {
                        editor.textProperty().addListener { _, _, newValue ->
                            if (!newValue.matches("\\d*".toRegex())) {
                                editor.text = newValue.replace("[^\\d]".toRegex(), "")
                            }
                        }
                        editor.textProperty().addListener { _, oldValue, newValue ->
                            if (newValue.toInt() > 65535) {
                                editor.text = oldValue
                            }
                        }
                    }
                }
                field("Db") {
                    spinner(enableScroll = true, min = 0, max = 16, property = newConnectionViewModel.db)
                }
            }
            field("Auth") {
                passwordfield(newConnectionViewModel.pass)
            }
            hbox {
                padding = insets(10, 0, 0, 0)
                alignment = Pos.BASELINE_RIGHT
                spacing = 20.0
                button("Test", SVGIcon(
                        "M512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832zM448 448h128v384H448V448z m0-256h128v128H448V192z",
                        color = c("1296db"),
                        size = 14
                )) {
                    spacing = 4.0
                    action {
                        newConnectionViewModel.commit {
                            // connectionController.test(newConnectionViewModel.item)
                            find<AshesTestConnectionFragment>(AshesTestConnectionFragment::connection to newConnectionViewModel.item).openModal()
                        }
                    }
                }
                region {
                    hgrow = Priority.ALWAYS
                }
                button("Cancel", SVGIcon("M595.392 504.96l158.4 158.336-90.496 90.496-158.4-158.4-158.4 158.4L256 663.296l158.4-158.4L256 346.496 346.496 256 504.96 414.4 663.296 256l90.496 90.496L595.392 504.96zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832z", color = c("d81e06"), size = 14)) .action{
                    newConnectionViewModel.rollback()
                    close()
                }
                button("Save", SVGIcon("M85.312 85.312v853.376h853.376v-512L597.312 85.312h-512zM0 0h640l384 384v640H0V0z m170.688 512v341.312h170.624V512H170.688z m256 0v341.312h170.624V512H426.688z m256 0v341.312h170.624V512h-170.624z m-512-341.312v256h680.192l-253.568-256H170.688z",
                        color = c("1296db"), size = 14
                )).action {
                    newConnectionViewModel.commit {
                        connectionController.save(newConnectionViewModel.item)
                        newConnectionViewModel.rebind {
                            itemProperty.set(AshesConnection(id = UUID.randomUUID().toString(), name = "", host = ""))
                        }
                    }
                    close()
                }
            }
        }
    }
}

class AshesEditConnectionFragment: AshesBaseFragment() {

    val current by param<AshesConnection>()

    private val connectionController by inject<AshesConnectionController>()

    private val editConnectionViewModel by inject<AshesEditConnectionItemViewModel>()

    init {
        editConnectionViewModel.rebind { itemProperty.set(current) }
    }

    override val root = form {
        title = "Edit Connection"
        fieldset {
            field("Id") {
                textfield(editConnectionViewModel.id)
                hide()
            }
            field("Name") {
                textfield(editConnectionViewModel.name) {
                    required()
                }
            }
            field("Host") {
                textfield(editConnectionViewModel.host) {
                    required()
                }
            }
            hbox {
                alignment = Pos.BASELINE_LEFT
                spacing = 20.0
                field("Type") {
                    combobox(values = listOf("Singleton", "Shared")) {
                        selectionModel.selectFirst()
                    }
                }
                field("Port") {
                    spinner(editable = true, enableScroll = true, min = 0, max = 65535, property = editConnectionViewModel.port) {
                        editor.textProperty().addListener { _, _, newValue ->
                            if (!newValue.matches("\\d*".toRegex())) {
                                editor.text = newValue.replace("[^\\d]".toRegex(), "")
                            }
                        }
                        editor.textProperty().addListener { _, oldValue, newValue ->
                            if (newValue.toInt() > 65535) {
                                editor.text = oldValue
                            }
                        }
                    }
                }
                field("Db") {
                    spinner(enableScroll = true, min = 0, max = 16, property = editConnectionViewModel.db)
                }
            }
            field("Auth") {
                passwordfield(editConnectionViewModel.pass)
            }
            hbox {
                padding = insets(10, 0, 0, 0)
                alignment = Pos.BASELINE_RIGHT
                spacing = 20.0
                button("Test", SVGIcon(
                        "M512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832zM448 448h128v384H448V448z m0-256h128v128H448V192z",
                        color = c("1296db"),
                        size = 14
                )) {
                    spacing = 4.0
                    action {
                        editConnectionViewModel.commit {
                            // connectionController.test(editConnectionViewModel.item)
                            find<AshesTestConnectionFragment>(AshesTestConnectionFragment::connection to editConnectionViewModel.item).openModal()
                        }
                    }
                }
                region {
                    hgrow = Priority.ALWAYS
                }
                button("Cancel", SVGIcon("M595.392 504.96l158.4 158.336-90.496 90.496-158.4-158.4-158.4 158.4L256 663.296l158.4-158.4L256 346.496 346.496 256 504.96 414.4 663.296 256l90.496 90.496L595.392 504.96zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832z", color = c("d81e06"), size = 14)) .action{
                    editConnectionViewModel.rollback()
                    close()
                }
                button("Save", SVGIcon("M85.312 85.312v853.376h853.376v-512L597.312 85.312h-512zM0 0h640l384 384v640H0V0z m170.688 512v341.312h170.624V512H170.688z m256 0v341.312h170.624V512H426.688z m256 0v341.312h170.624V512h-170.624z m-512-341.312v256h680.192l-253.568-256H170.688z",
                        color = c("1296db"), size = 14
                )).action {
                    editConnectionViewModel.commit {
                        connectionController.update(current, editConnectionViewModel.item)
                    }
                    close()
                }
            }
        }
    }
}

class AshesTestConnectionFragment: AshesBaseFragment() {

    private val property = SimpleDoubleProperty(0.0)

    private val connectionController by inject<AshesConnectionController>()

    val connection by param<AshesConnection>()

    private val data = SimpleListProperty(connectionController.test(connection).observable())

    override val root = stackpane {
        title = "Test Connection"
        vbox {
            spacing = 10.0
            padding= insets(10, 0, 10, 0)
            tableview(data) {
                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS
                alignment = Pos.CENTER
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                readonlyColumn("Command", Pair<String, String>::first) {
                    isSortable = false
                }
                readonlyColumn("Status", Pair<String, String>::second) {
                    isSortable = false
                }
                fixedCellSize = 30.0
                prefHeightProperty().bind(Bindings.size(items).multiply(fixedCellSize).add(30.0))
            }
            hbox {
                alignment = Pos.BASELINE_RIGHT
                padding = insets(10, 0)
                button("Close") .action {
                    property.set(0.0)
                    close()
                }
            }
        }
    }
}


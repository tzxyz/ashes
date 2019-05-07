package org.tzxyz.ashes.views

import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.util.Duration
import org.tzxyz.ashes.controllers.AshesConnectionController
import org.tzxyz.ashes.viewmodels.AshesConnectionItemViewModel
import tornadofx.*

class AshesConnectionView: AshesBaseView() {

    private val connectionController by inject<AshesConnectionController>()
    
    private val connectionViewModel by inject<AshesConnectionItemViewModel>()

    override val root = form {
        title = "New Connection"
        fieldset {
            field("Name") {
                textfield(connectionViewModel.name) {
                    required()
                }
            }
            field("Host") {
                textfield(connectionViewModel.host) {
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
                    spinner(editable = true, enableScroll = true, min = 0, max = 65535, property = connectionViewModel.port) {
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
                    spinner(enableScroll = true, min = 0, max = 16, property = connectionViewModel.db)
                }
            }
            field("Auth") {
                passwordfield(connectionViewModel.pass)
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
                        connectionViewModel.commit {
                            connectionController.testConnection(connectionViewModel.item)
                            openInternalWindow(AshesTestConnectionView::class, owner = this@form)
                        }
                    }
                }
                region {
                    hgrow = Priority.ALWAYS
                }
                button("Cancel", SVGIcon("M595.392 504.96l158.4 158.336-90.496 90.496-158.4-158.4-158.4 158.4L256 663.296l158.4-158.4L256 346.496 346.496 256 504.96 414.4 663.296 256l90.496 90.496L595.392 504.96zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m3.008-92.992a416 416 0 1 0 0-832 416 416 0 0 0 0 832z", color = c("d81e06"), size = 14)) .action{
                    connectionViewModel.rollback()
                    close()
                }
                button("Save", SVGIcon("M85.312 85.312v853.376h853.376v-512L597.312 85.312h-512zM0 0h640l384 384v640H0V0z m170.688 512v341.312h170.624V512H170.688z m256 0v341.312h170.624V512H426.688z m256 0v341.312h170.624V512h-170.624z m-512-341.312v256h680.192l-253.568-256H170.688z",
                        color = c("1296db"), size = 14
                )).action {
                    connectionViewModel.commit {
                        connectionController.saveConnection(connectionViewModel.item)
                    }
                    close()
                }
            }
        }
    }
}

class AshesTestConnectionView: View() {

    private val property = SimpleDoubleProperty(0.0)

    override val root = stackpane {
        vbox {
            text("redis://localhost:6379")
            progressbar {
                progressProperty().animate(1.0, Duration(500.0))
            }
            button("Ok") .action {
                property.set(0.0)
                close()
            }
        }
    }

}
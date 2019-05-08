package org.tzxyz.ashes.views

import com.jfoenix.controls.JFXRippler
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import org.tzxyz.ashes.fragments.AshesNewConnectionFragment
import tornadofx.*

class AshesTopView: AshesBaseView() {

    private val new = JFXRippler(FontAwesomeIconView(FontAwesomeIcon.PLUS, "1.5em"), JFXRippler.RipplerPos.BACK)

    override val root = vbox {
        alignment = Pos.CENTER
        menubar {
            isUseSystemMenuBar = true
            menu {
                text = "File"
                item("New Connection")
            }
            menu {
                text = "Edit"
            }
            menu {
                text = "Run"
            }
        }
        hbox {
            vgrow = Priority.ALWAYS
            stackpane {
                style {
                    padding = box(10.px)
                }
                add(new)
            }
            stackpane {
                style {
                    padding = box(10.px)
                }
                add(JFXRippler(FontAwesomeIconView(FontAwesomeIcon.DATABASE, "1.5em"), JFXRippler.RipplerPos.BACK))
            }
            stackpane {
                style {
                    padding = box(10.px)
                }
                add(JFXRippler(FontAwesomeIconView(FontAwesomeIcon.MAGIC, "1.5em"), JFXRippler.RipplerPos.BACK))
            }
            stackpane {
                style {
                    padding = box(10.px)
                }
                add(JFXRippler(FontAwesomeIconView(FontAwesomeIcon.CODE, "1.5em"), JFXRippler.RipplerPos.BACK))
            }
        }
    }

    init {
        new.setOnMouseClicked { _ ->
            find<AshesNewConnectionFragment>().openModal()
        }
    }

}
package io.github.tzxyz.ashes.utils

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region

class DragResizer private constructor(private val region: Region) {

    private var x: Double = 0.toDouble()

    private var initMinWith: Boolean = false

    private var dragging: Boolean = false

    private fun mouseReleased(event: MouseEvent) {
        dragging = false
        region.cursor = Cursor.DEFAULT
    }

    private fun mouseOver(event: MouseEvent) {
        if (isInDraggableZone(event) || dragging) {
            region.cursor = Cursor.W_RESIZE
        } else {
            region.cursor = Cursor.DEFAULT
        }
    }

    private fun isInDraggableZone(event: MouseEvent): Boolean {
        return event.x > region.width - RESIZE_MARGIN
    }

    private fun mouseDragged(event: MouseEvent) {
        if (!dragging) {
            return
        }

        val mousey = event.x

        val minWidth = region.minWidth + (mousey - x)

        if (x > region.minWidth) {
            region.minWidth = minWidth
        }

        x = mousey
    }

    protected fun mousePressed(event: MouseEvent) {

        if (!isInDraggableZone(event)) {
            return
        }

        dragging = true

        if (!initMinWith) {
            region.minWidth = region.width
            initMinWith = true
        }

        x = event.x
    }

    companion object {
        const val RESIZE_MARGIN = 5
        fun makeResizable(region: Region) {
            val resizer = DragResizer(region)
            region.onMousePressed = EventHandler { event -> resizer.mousePressed(event) }
            region.onMouseDragged = EventHandler { event -> resizer.mouseDragged(event) }
            region.onMouseMoved = EventHandler { event -> resizer.mouseOver(event) }
            region.onMouseReleased = EventHandler { event -> resizer.mouseReleased(event) }
        }
    }
}
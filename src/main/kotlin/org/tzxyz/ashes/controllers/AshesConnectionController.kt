package org.tzxyz.ashes.controllers

import org.tzxyz.ashes.events.AshesNewConnectionEvent
import org.tzxyz.ashes.models.AshesConnection
import org.tzxyz.ashes.utils.JsonUtils

class AshesConnectionController: AshesBaseController() {

    fun loadConnections(): List<AshesConnection> {
        return JsonUtils.fromJsonString(config.string("connections", "[]"))
    }

    fun saveConnection(connection: AshesConnection) {
        with(config) {
            val connections = JsonUtils.fromJsonString<List<AshesConnection>>(config.string("connections", "[]")).toMutableList()
            connections.add(connection)
            set("connections", JsonUtils.toJsonString(connections))
            save()
            fire(AshesNewConnectionEvent(connection))
        }
    }

    fun testConnection(connection: AshesConnection): Boolean {
        return true
    }
}
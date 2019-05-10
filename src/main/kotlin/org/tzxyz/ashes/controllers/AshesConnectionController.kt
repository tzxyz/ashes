package org.tzxyz.ashes.controllers

import org.tzxyz.ashes.events.AshesNewConnectionEvent
import org.tzxyz.ashes.events.AshesScanKeyEvent
import org.tzxyz.ashes.events.AshesUpdateConnectionEvent
import org.tzxyz.ashes.global.Current
import org.tzxyz.ashes.models.AshesConnection
import org.tzxyz.ashes.redis.AshesRedisClientFactory
import org.tzxyz.ashes.utils.JsonUtils
import redis.clients.jedis.ScanParams

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

    fun updateConnection(before: AshesConnection, update: AshesConnection): AshesConnection {
        with(config) {
            val connections = JsonUtils.fromJsonString<List<AshesConnection>>(config.string("connections", "[]")).toMutableList()
            val index = connections.indexOf(before)
            connections[index] = update
            set("connections", JsonUtils.toJsonString(connections))
            save()
            fire(AshesUpdateConnectionEvent(before, update))
        }
        return update
    }

    fun testConnection(connection: AshesConnection): Boolean {
        return true
    }

    fun connect(connection: AshesConnection) {
        val client = AshesRedisClientFactory.get(connection)
        Current.setConnection(connection)
        runAsync {
            val keys = mutableListOf<String>()
            var cursor = "0"
            do {
                val scanResult = client.scan(cursor, ScanParams().match("*").count(10000))
                val scanKeys = scanResult.result
                keys.addAll(scanKeys)
                cursor = scanResult.cursor
            } while (cursor != "0")
            keys.sort()
            fire(AshesScanKeyEvent(connection, keys))
        }

    }
}
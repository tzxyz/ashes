package io.github.tzxyz.ashes.controllers

import io.github.tzxyz.ashes.events.AshesNewConnectionEvent
import io.github.tzxyz.ashes.events.AshesScanKeyEvent
import io.github.tzxyz.ashes.events.AshesUpdateConnectionEvent
import io.github.tzxyz.ashes.global.Current
import io.github.tzxyz.ashes.models.AshesConnection
import io.github.tzxyz.ashes.redis.AshesRedisClientFactory
import io.github.tzxyz.ashes.utils.JsonUtils
import redis.clients.jedis.ScanParams

class AshesConnectionController: AshesBaseController() {

    fun load(): List<AshesConnection> {
        return JsonUtils.fromJsonString(config.string("connections", "[]"))
    }

    fun save(connection: AshesConnection) {
        with(config) {
            val connections = JsonUtils.fromJsonString<List<AshesConnection>>(config.string("connections", "[]")).toMutableList()
            connections.add(connection)
            set("connections", JsonUtils.toJsonString(connections))
            save()
            fire(AshesNewConnectionEvent(connection))
        }
    }

    fun update(before: AshesConnection, update: AshesConnection): AshesConnection {
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

    fun test(connection: AshesConnection): List<Pair<String, String>> {
        val client = AshesRedisClientFactory.get(connection)
        if (connection.password == null) {
            return listOf("CONNECT" to "OK", "PING" to client.ping(), "SELECT" to client.select(connection.db))
        } else {
            return listOf("CONNECT" to "OK", "PING" to client.ping(), "Auth" to client.auth(connection.password), "SELECT" to client.select(connection.db))
        }
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
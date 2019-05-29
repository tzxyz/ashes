package io.github.tzxyz.ashes.global

import io.github.tzxyz.ashes.models.AshesConnection
import java.util.concurrent.ConcurrentHashMap

class Current {

    companion object {

        private val data = ConcurrentHashMap<String, Any>()

        fun get(key: String): Any? {
            return data[key]
        }

        fun set(key: String, value: Any) {
            data[key] = value
        }

        fun setConnection(value: AshesConnection) {
            set("connection", value)
        }

        fun getConnection(): AshesConnection {
            return get("connection") as AshesConnection
        }
    }
}
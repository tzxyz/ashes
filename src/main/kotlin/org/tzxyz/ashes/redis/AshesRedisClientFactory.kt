package org.tzxyz.ashes.redis

import org.tzxyz.ashes.models.AshesConnection
import redis.clients.jedis.BinaryJedis
import redis.clients.jedis.Jedis
import java.util.concurrent.ConcurrentHashMap

class AshesRedisClientFactory {

    companion object {

        private val connections = ConcurrentHashMap<AshesConnection, Jedis>()

        fun get(connection: AshesConnection): Jedis {
            if (connections[connection] == null) {
                val client = Jedis()
                val field = BinaryJedis::class.java.getDeclaredField("client")
                field.isAccessible = true
                val ashes = AshesRedisClient(connection.host, connection.port)
                field.set(client, ashes)
                if (connection.password != null) {
                    client.auth(connection.password)
                }
                if (connection.db != 0) {
                    client.select(connection.db)
                }
                connections[connection] = client
            }
            return connections[connection]!!
        }

    }
}
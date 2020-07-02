package io.github.tzxyz.ashes.services

import io.github.tzxyz.ashes.redis.AshesRedisRawCommand
import io.lettuce.core.RedisClient

class AshesConsoleService: AshesBaseService() {

    val client = RedisClient.create("redis://localhost")
    val connection = client.connect()

    fun execute(command: AshesRedisRawCommand): String {
        if (command.args() == null) {
            val result = connection.sync().dispatch(command.type(), command.output())
            println(result)
            return formatResult(result)
        } else {
            val result = connection.sync().dispatch(command.type(), command.output(), command.args())
            return formatResult(result)
        }
    }

    fun formatResult(result: Any): String {
        return when (result) {
            is String -> result
            else -> result.toString()
        }
    }
}
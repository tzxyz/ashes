package io.github.tzxyz.ashes.services

import io.github.tzxyz.ashes.redis.AshesRedisRawCommand
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisCommandExecutionException

class AshesConsoleService: AshesBaseService() {

    val client = RedisClient.create("redis://localhost")
    val connection = client.connect()

    fun execute(command: AshesRedisRawCommand): String {
        try {
            if (command.args() == null) {
                val result = connection.sync().dispatch(command.type(), command.output())
                return formatResult(result)
            } else {
                val result = connection.sync().dispatch(command.type(), command.output(), command.args())
                return formatResult(result)
            }
        } catch (e: RedisCommandExecutionException) {
            return "(error) ${e.message}"
        }

    }

    fun formatResult(result: Any?): String {
        return when (result) {
            null -> "(nil)"
            is String -> result
            else -> result.toString()
        }
    }
}
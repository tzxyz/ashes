package io.github.tzxyz.ashes.redis

import io.lettuce.core.codec.StringCodec
import io.lettuce.core.output.CommandOutput
import io.lettuce.core.output.StatusOutput
import io.lettuce.core.output.ValueOutput
import io.lettuce.core.protocol.CommandArgs
import io.lettuce.core.protocol.CommandType
import io.lettuce.core.protocol.CommandType.*
import java.lang.RuntimeException

data class AshesRedisRawCommand(val input: String) {

    private val codec = StringCodec.UTF8

    private val array = input.split("\\s+".toRegex())

    fun type(): CommandType {
        return CommandType.valueOf(array[0].toUpperCase())
    }

    fun output(): CommandOutput<String, String, *> {

        return when (type()) {
            INFO, SET, PING -> StatusOutput<String, String>(codec)
            GET -> ValueOutput<String, String>(codec)
            else -> throw RuntimeException("can't find command output for %s".format(type()))
        }

    }

    fun args(): CommandArgs<String, String>? {
        if (array.size == 1) {
            return null
        }
        if (array.size == 2) {
            return CommandArgs<String, String>(StringCodec.UTF8).addKey(array[1])
        }
        val args = array.subList(2, array.size).toTypedArray()
        return CommandArgs<String, String>(StringCodec.UTF8).addKey(array[1]).addValues(*args)
    }

    fun resultType(): Class<*> {
        return when(type()) {
            SET, GET -> String::class.java
            else -> String::class.java
        }
    }
}
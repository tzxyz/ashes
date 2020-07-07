package io.github.tzxyz.ashes.redis

import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.output.CommandOutput
import io.lettuce.core.output.ListSubscriber
import io.lettuce.core.output.StreamingOutput
import io.lettuce.core.protocol.CommandArgs
import io.lettuce.core.protocol.CommandType
import java.nio.ByteBuffer

data class AshesRedisRawCommand(val input: String) {

    private val codec = StringCodec.UTF8

    private val array = input.split("\\s+".toRegex())

    fun type(): CommandType {
        return CommandType.valueOf(array[0].toUpperCase())
    }

    fun output(): CommandOutput<String, String, *> {
        return AshesCommandOutput(codec)
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

    class AshesCommandOutput(codec: RedisCodec<String, String>): CommandOutput<String, String, String>(codec, null), StreamingOutput<String> {

        private var isMulti = false
        private var subscriber: StreamingOutput.Subscriber<String>? = null
        private var listOutput: List<String>? = null

        override fun set(bytes: ByteBuffer?) {
            if (isMulti) {
                subscriber?.onNext(listOutput, if (bytes == null) null else "\"${codec.decodeValue(bytes)}\"")
                output = (0..listOutput!!.size).toList().zip(listOutput!!).map { (k, v) -> "${k + 1}) $v" }.joinToString(System.lineSeparator())
            } else {
                if (bytes == null) output = "(nil)"
                else output = "\"${codec.decodeValue(bytes)}\""
            }
        }

        override fun set(integer: Long) {
            output = "(integer) $integer"
        }

        override fun multi(count: Int) {
            isMulti = true
            subscriber = ListSubscriber.instance()
            listOutput = ArrayList(count)
        }

        override fun getSubscriber(): StreamingOutput.Subscriber<String> {
            return subscriber!!
        }

        override fun setSubscriber(subscriber: StreamingOutput.Subscriber<String>?) {
            this.subscriber = subscriber
        }
    }
}
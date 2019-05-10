package org.tzxyz.ashes.redis

import org.tzxyz.ashes.events.AshesSendCommandEvent
import redis.clients.jedis.Client
import redis.clients.jedis.commands.ProtocolCommand
import redis.clients.jedis.util.SafeEncoder
import tornadofx.FX

class AshesRedisClient (host: String, port: Int): Client(host, port) {

    override fun sendCommand(cmd: ProtocolCommand?, vararg args: ByteArray?) {
        super.sendCommand(cmd, *args)
        val sb = StringBuilder()
        sb.append("(%s:%s):> ".format(host, port))
        sb.append(cmd)
        sb.append("  ")
        for (arg in args) {
            sb.append("%s  ".format(SafeEncoder.encode(arg)))
        }
        sb.append(System.lineSeparator())
        FX.eventbus.fire(AshesSendCommandEvent(sb.toString()))
    }

}

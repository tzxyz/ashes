package org.tzxyz.ashes

import org.junit.Test
import org.tzxyz.ashes.models.AshesConnection
import org.tzxyz.ashes.redis.AshesRedisClientFactory
import org.tzxyz.ashes.utils.JsonUtils
import java.time.LocalDateTime

class AshesApplicationTest {

    private val client = AshesRedisClientFactory.get(AshesConnection(name = "test", host = "127.0.0.1", port = 6379))

    @Test
    fun mockString() {
        val keys = client.keys("*String*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 30) {
            client.set("AshesKey:String:%s".format(i), i.toString().repeat(300))
            client.expire("AshesKey:String:%s".format(i), 30000)
        }
    }

    data class A(val a: Int = 1, val b: String = "123", val c: LocalDateTime = LocalDateTime.now())

    @Test
    fun mockJsonString() {
        val keys = client.keys("*JsonString*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 500_000_0) {
            client.set("AshesKey:JsonString:%s".format(i), JsonUtils.toJsonString(A()))
            client.expire("AshesKey:JsonString:%s".format(i), 30000)
        }
    }


    @Test
    fun mockHash() {
        val keys = client.keys("*Hash*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 100) {
            client.hset("AshesKey:Hash1", i.toString(), i.toString().repeat(30))
            client.hset("AshesKey:Hash2", i.toString(), i.toString().repeat(30))
            client.hset("AshesKey:Hash3", i.toString(), i.toString().repeat(30))
            client.hset("AshesKey:Hash4", i.toString(), i.toString().repeat(30))
            client.hset("AshesKey:Hash5", i.toString(), i.toString().repeat(30))
            client.expire("AshesKey:Hash1", 3600)
            client.expire("AshesKey:Hash2", 3600)
            client.expire("AshesKey:Hash3", 3600)
            client.expire("AshesKey:Hash4", 3600)
            client.expire("AshesKey:Hash5", 3600)
        }
    }

    @Test
    fun mockList() {
        val keys = client.keys("*List*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 30) {
            client.lpush("AshesKey:List:%s".format(i), "0")
            client.lpushx("AshesKey:List:%s".format(i), *(1..1000).map { it.toString() }.toTypedArray())
        }
    }

    @Test
    fun mockSet() {
        val keys = client.keys("*Set*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 10) {
            client.sadd("AshesKey:Set:%s".format(i), JsonUtils.toJsonString(A()))
            client.sadd("AshesKey:Set:%s".format(i), *(1..10).map { JsonUtils.toJsonString(A()) }.toTypedArray())
        }
    }

    @Test
    fun mockSortedSet() {
        val keys = client.keys("*ZSet*")
        keys.forEach { client.del(it) }
        for (i in 1 .. 10) {
            client.zadd("AshesKey:ZSetAshesKey:ZSetAshesKey:ZSetAshesKey:ZSetAshesKey:ZSetAshesKey:ZSetAshesKey:ZSet:%s".format(i), 0.0, "0")
            client.zadd("AshesKey:ZSet:%s".format(i), mapOf(Pair("1", 1.0), Pair("2", 2.0), Pair("3", 3.0)))
        }
    }

    @Test
    fun mockKey() {
        mockString()
        mockList()
        mockHash()
        mockSet()
        mockSortedSet()
    }

    @Test
    fun flushAll() {
        client.flushAll()
    }

}
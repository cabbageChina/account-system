package com.aduer.platform.member

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.RedisZSetCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Service
class RedisService(
        val objectMapper: ObjectMapper,
        val redisTemplate: RedisTemplate<String, String>
) {
    companion object {
        // 默认存活时间
        private val defaultActiveTime = 60L * 1000
    }

    fun lock(key: String, seconds: Int = 5): Boolean {
        return redisTemplate.execute {
            val lock = it.setNX(key.toByteArray(), "lock".toByteArray())
            if (lock && seconds > 0) {
                it.expire(key.toByteArray(), seconds.toLong())
            }
            lock
        }
    }

    fun lock(key: String, seconds: Int = 5, handler: () -> Unit): Boolean {
        val flag = this.lock(key, seconds)
        if (!flag) return false
        try {
            handler()
        } catch (e: Exception) {
            throw e
        } finally {
            this.unlock(key)
        }
        return true
    }

    fun unlock(key: String) {
        redisTemplate.execute {
            it.del(key.toByteArray())
        }
    }

    fun set(key: String, any: Any, activeTime: Long = -1) {
        redisTemplate.execute {
            it.set(key.toByteArray(), objectMapper.writeValueAsBytes(any))

            if (activeTime != -1L) {
                it.expire(key.toByteArray(), activeTime)
            }
        }
    }

/*    fun <T : Any> get(key: String, clz: Class<T>): T? {
        return redisTemplate.execute({connection: RedisConnection? ->
            if (connection == null) {
                null
            } else {
                val byteArrays = connection.get(key.toByteArray())
                objectMapper.readValue(byteArrays, clz)
            }
        })
//        val any = redisTemplate.opsForValue().get(key) ?: return null
//        return any as T
    }*/

    fun <T : Any> get(key: String, clz: Class<T>): T? {
//        return null
        return redisTemplate.execute { connection ->
            val byteArrays = connection.get(key.toByteArray())
            if (byteArrays != null) {
                objectMapper.readValue(byteArrays, clz)
            } else {
                null
            }
        }
    }


//    fun <T> getList(key: String): List<T>? {
//        val any = redisTemplate.opsForValue().get(key) ?: return null
//        return any as List<T>
//    }

    fun <T> getList(key: String, clz: Class<out T>): List<T>? {
        return redisTemplate.execute { connection ->
            val type = objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, clz)
            val byteArrays = connection.get(key.toByteArray())
            if (byteArrays == null) {
                null
            } else {
                objectMapper.readValue(byteArrays, type)
            }
        }
//        return null
    }

    fun delete(key: String) {
        redisTemplate.delete(key)

    }

    fun delete(keys: List<String>) {
        redisTemplate.delete(keys)
    }

    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)


    fun autoIncrement(key: String, value: Long? = null, activeTime: Long? = null): Long {
        return redisTemplate.execute({
            val v = if (value == null) {
                it.incr(key.toByteArray())
            } else {
                this.incrBy(key, value, activeTime)
            }
            if (activeTime != null) {
                it.expire(key.toByteArray(), activeTime)
            }
            v
        }, true)
    }

    fun autoDecr(key: String, value: Long? = null): Long {
        return redisTemplate.execute({
            if (value == null) {
                it.decr(key.toByteArray())
            } else {
                it.decrBy(key.toByteArray(), value)
            }
        }, true)
    }

    @Synchronized
    private fun incrBy(key: String, value: Long, activeTime: Long? = null): Long {
        return if (!this.hasKey(key)) {
            this.autoIncrement(key, activeTime = activeTime)
        } else {
            redisTemplate.execute({
                it.incrBy(key.toByteArray(), value)
            }, true)
        }
    }

    fun setAutoIncrement(key: String, value: Long = 1, activeTime: Long? = null): Long {
        return redisTemplate.execute({
            val x = it.incrBy(key.toByteArray(), value)

            if (activeTime != null) {
                it.expire(key.toByteArray(), activeTime)
            }
            x

        }, true)
    }

    fun pushElementForSortedSet(key: String, any: Any, score: Double) {
        return redisTemplate.execute({

            //val byteValue= (any as? String)?.toByteArray() ?: objectMapper.writeValueAsBytes(any)
            it.zAdd(key.toByteArray(), score.toDouble(), objectMapper.writeValueAsBytes(any))
        }, true)
    }

    fun removeElementForSortedSetByScope(key: String, score: Double) {
        return redisTemplate.execute({
            it.zRemRangeByScore(key.toByteArray(), RedisZSetCommands.Range.range().gte(score).lte(score))
        }, true)
    }

    fun removeElement(key: String, any: Any) {
        return redisTemplate.execute({
            it.zRem(key.toByteArray(), objectMapper.writeValueAsBytes(any))
        }, true)
    }

    fun <T> getElementForSortedSet(key: String, clz: Class<T>): List<T> {
        return redisTemplate.execute {

            val setBytes = it.zRange(key.toByteArray(), 0, -1)
            setBytes?.map { objectMapper.readValue(it, clz) }?.toList() ?: emptyList()
        }
    }

    fun <T> getElementsForSortedSet(key: String, clz: Class<T>, min: Double, max: Double): List<T> {

        return redisTemplate.execute({
            val data = it.zRevRangeByScore(key.toByteArray(), min, max)
            if (data.isEmpty()) {
                emptyList()
            } else {
                data.map { objectMapper.readValue(it, clz) }
            }

        }, true)

    }

    /*fun pushElementForMap(key: String, mapKey: String, mapValue: Any) {
        return redisTemplate.execute({
            it.hSet(key.toByteArray(), mapKey.toByteArray(), objectMapper.writeValueAsBytes(mapValue))
        })
    }

    fun removeElementForMap(key: String, mapKey: String) {

        return redisTemplate.execute({
            it.hDel()
        })

    }*/

    fun <T> getList(redisKey: String, clz: Class<out T>, dbQuery: () -> List<T>, activeTime: Long? = null): List<T> {
        val data = this.getList(redisKey, clz)
        if (data == null) {
            val dbData = dbQuery()
            this.set(redisKey, dbData, activeTime ?: -1)
            return dbData
        }
        return data
    }

    fun <T> getList(redisKey: String, clz: Class<out T>, activeTime: Long? = null, dbQuery: () -> List<T>): List<T> {
        val data = this.getList(redisKey, clz)
        if (data == null) {
            val dbData = dbQuery()
            this.set(redisKey, dbData, activeTime ?: -1)
            return dbData
        }
        return data
    }


    fun <T : Any> get(redisKey: String, clz: Class<out T>, dbQuery: () -> T?, activeTime: Long? = null): T? {
        val data = this.get(redisKey, clz)

        if (data == null) {
            val dbData = dbQuery() ?: return null
            this.set(redisKey, dbData, activeTime ?: -1)
            return dbData

        }
        return data
    }


    fun <T : Any> get(redisKey: String, clz: Class<out T>, activeTime: Long? = null, dbQuery: () -> T?): T? {
        val data = this.get(redisKey, clz)

        if (data == null) {
            val dbData = dbQuery() ?: return null
            this.set(redisKey, dbData, activeTime ?: -1)
            return dbData

        }
        return data
    }


}
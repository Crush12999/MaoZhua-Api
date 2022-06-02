package com.maozhua.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author sryzzz
 * @create 2022/6/2 19:08
 * @description Redis 操作封装工具类
 */
@Component
public class RedisOperator {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean keyIsExist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key 键
     * @return key的剩余生存时间
     */
    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 实现命令：expire 设置过期时间，单位秒
     *
     * @param key 键
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：increment key，增加key一次
     *
     * @param key 键
     * @return 增加后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 累加，使用hash
     *
     * @param name  hash的名称（redis对应的键）
     * @param key   name对应hash结构的key
     * @param delta name对应hash结构的值以delta自增
     * @return 自增后的值
     */
    public Long incrementHash(String name, String key, long delta) {
        return redisTemplate.opsForHash().increment(name, key, delta);
    }

    /**
     * 累减，使用hash
     *
     * @param name  hash的名称（redis对应的键）
     * @param key   name对应hash结构的key
     * @param delta name对应hash结构的值以delta自减
     * @return 自减后的值
     */
    public Long decrementHash(String name, String key, long delta) {
        delta = delta * (-1);
        return redisTemplate.opsForHash().increment(name, key, delta);
    }

    /**
     * hash 设置value
     */
    public void setHashValue(String name, String key, String value) {
        redisTemplate.opsForHash().put(name, key, value);
    }

    /**
     * hash 获得value
     */
    public String getHashValue(String name, String key) {
        return (String) redisTemplate.opsForHash().get(name, key);
    }

    /**
     * 实现命令：decrement key，减少key一次
     *
     * @param key 键
     * @return 减少后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key 键
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    // String（字符串）

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（以秒为单位）
     */
    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key   键
     * @param value 值
     */
    public void setnx60s(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key   键
     * @param value 值
     */
    public void setnx(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key 键
     * @return value
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量查询，对应mget
     *
     * @param keys 键数组
     * @return 结果列表
     */
    public List<String> mGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 批量查询，管道pipeline
     *
     * @param keys 键数组
     * @return 结果列表
     */
    public List<Object> batchGet(List<String> keys) {

        //		nginx -> keepalive
        //		redis -> pipeline

        List<Object> result = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection src = (StringRedisConnection) connection;

                for (String k : keys) {
                    src.get(k);
                }
                return null;
            }
        });

        return result;
    }


    // Hash（哈希表）

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key   键
     * @param field 域
     * @param value 值
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     *
     * @param key   键
     * @param field 域
     * @return 值
     */
    public String hGet(String key, String field) {
        return String.valueOf(redisTemplate.opsForHash().get(key, field));
    }

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key    键
     * @param fields 域列表
     */
    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     *
     * @param key 键
     * @return 所有的域和值
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // List（列表）

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key   键
     * @param value 值
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public Long lPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key 键
     * @return 列表key的头元素。
     */
    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key   键
     * @param value 值
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public Long rPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

}

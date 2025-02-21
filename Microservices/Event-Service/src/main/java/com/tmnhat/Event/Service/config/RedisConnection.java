package com.tmnhat.Event.Service.config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
    private static JedisPool jedisPool;

    // Khởi tạo Redis connection
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);

        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000, null);
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }
}

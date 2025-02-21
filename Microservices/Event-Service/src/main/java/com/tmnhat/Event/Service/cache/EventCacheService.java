package com.tmnhat.Event.Service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tmnhat.Event.Service.config.RedisConnection;
import com.tmnhat.Event.Service.model.Event;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventCacheService {
    private static final String HOT_EVENT_KEY = "hot_events";
    private static final int EXPIRE_TIME_IN_SECONDS = 86400; // 1 day (24h)

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    public EventCacheService() {
        this.jedisPool = RedisConnection.getJedisPool(); // Lấy Redis connection từ RedisConnection
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule

    }
    /**
     * Store hot event in Redis with TTL of 1 day.
     */
    public void cacheHotEvent(Event event) {
        try (Jedis jedis = jedisPool.getResource()) {
            String eventJson = objectMapper.writeValueAsString(event); //convert event - > json
            jedis.hset(HOT_EVENT_KEY, event.getId().toString(), eventJson); //luư vào redis hash
            jedis.expire(HOT_EVENT_KEY, EXPIRE_TIME_IN_SECONDS);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing Event: " + e.getMessage());
        }
    }

    /**
     * Retrieve hot event from Redis.
     */
    public Optional<Event> getHotEvent(Long eventId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String eventJson = jedis.hget(HOT_EVENT_KEY, eventId.toString()); // Lấy Json từ redis
            if (eventJson != null) {
                return Optional.of(objectMapper.readValue(eventJson, Event.class)); //Convert Json - > Event
            }
        } catch (Exception e) {
            System.err.println("Error retrieving Event from Redis: " + e.getMessage());
        }
        return Optional.empty();
    }
    public List<Event> getAllHotEvents() {
        List<Event> hotEvents = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> events = jedis.hgetAll(HOT_EVENT_KEY);
            for (String eventJson : events.values()) {
                hotEvents.add(objectMapper.readValue(eventJson, Event.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotEvents;
    }
    /**
     * Remove hot event from cache.
     */
    public void removeHotEvent(Long eventId) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(HOT_EVENT_KEY, eventId.toString()); //Xoá khỏi redis
        } catch (Exception e) {
            System.err.println("Error deleting Event from Redis: " + e.getMessage());
        }
    }
}

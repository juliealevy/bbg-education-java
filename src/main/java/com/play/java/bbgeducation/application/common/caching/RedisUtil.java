package com.play.java.bbgeducation.application.common.caching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<T> {
    private RedisTemplate<String, T> redisTemplate;
    private ValueOperations<String, T> valueOperations;

    RedisUtil(RedisTemplate<String, T> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void putValue(String key,T value) {
        putValueWithExpireTime(key, value, 1, TimeUnit.HOURS);
    }

    public void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
        if (value == null){
            throw new IllegalArgumentException("Cannot cache a null value");
        }

        valueOperations.set(key, value, timeout, unit);
    }
    public Optional<T> getValue(String key) {
        if (!hasKey(key)){
            return Optional.empty();
        }
        return Optional.of(valueOperations.get(key));
    }

    public void deleteValue(String key){
        redisTemplate.delete(key);
    }

    public void clearAll(){
        redisTemplate.keys("*").stream()
                .forEach(k -> {
                    redisTemplate.delete(k);
                });
    }
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public int getCount() {
        return redisTemplate.keys("*").size();
    }
}

package com.myxiaowang.logistics.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年11月30日 14:19:00
 */
@Component

public class RedisTemplateUtil<V> {

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, V> redisTemplate;

    /**
     * 数据检查，如果不存在执行supplier返回得结果
     * 如果传入的值没有获取到，就设置一个默认的值
     * @param key      redis Key
     * @param supplier 制造者
     */
    public V get(String key, Supplier<V> supplier) {
        return Optional.ofNullable(get(key)).orElse(supplier.get());
    }

    /**
     * 数据检查，如果不存在执行supplier得结果，且执行consumer
     *
     * @param key      redis Key
     * @param supplier 制造者
     * @param consumer 消费者
     */
    public V get(String key, Supplier<V> supplier, Consumer<V> consumer) {
        V v = get(key);
        if (Objects.isNull(v)) {
            V check = supplier.get();
            consumer.accept(check);
            return check;
        }
        return v;
    }

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间 -1为无期限
     * @param timeUnit 时间格式
     */
    public void set(String key, V value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     *
     * @param kvMap 键值map
     */
    public void batchSet(Map<String, V> kvMap) {
        redisTemplate.opsForValue().multiSet(kvMap);
    }


    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param kvMap 键值map
     */
    public void batchSetIfAbsent(Map<String, V> kvMap) {
        redisTemplate.opsForValue().multiSetIfAbsent(kvMap);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number Long
     */
    public Long increment(String key, Long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number Double
     */
    public Double increment(String key, Double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 删除key
     *
     * @param key 键
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除
     *
     * @param keys 键的集合
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置Key的过期时间
     *
     * @param key 键
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    public Boolean expire(String key, Long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 获取key的过期时间
     *
     * @param key 键
     * @return 过期时间，单位分钟
     */
    public Long getExpire(String key){
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    public ListOperations<String, V> opsForList(){
        return redisTemplate.opsForList();
    }

}

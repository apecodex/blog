package cn.apecode.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: Redis接口类
 * @author: apecode
 * @date: 2021-10-18 17:24
 **/
@SuppressWarnings("all")
public interface RedisService {

    /**
     * @description: 保存属性
     * @param key
     * @param value
     * @param time
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    void set(String key, Object value, long time);

    /**
     * @description: 保存属性
     * @param key
     * @param value
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    void set(String key, Object value);

    /**
     * @description: 获取属性
     * @param key
     * @return {@link Object}
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    Object get(String key);

    /**
     * @description: 删除属性
     * @param key
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    Boolean del(String key);

    /**
     * @description: 批量删除属性
     * @param keys
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    Long del(List<String> keys);

    /**
     * @description: 设置过期时间
     * @param key
     * @param time
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 18:57
    */
    Boolean expire(String key, long time);

    /**
     * @description: 获取过期时间
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 18:58
    */
    Long getExpire(String key);

    /**
     * @description: 判断key是否存在
     * @param key
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 18:58
    */
    Boolean hasKey(String key);

    /**
     * @description: 按delta递增
     * @param key
     * @param delta
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 18:58
    */
    Long incr(String key, long delta);

    /**
     * @description: 设定过期时间的递增1
     * @param key
     * @param time
     * @return
     */
    Long incrExpire(String key, long time);

    /**
     * @description: 按delta递减
     * @param key
     * @param delta
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 18:58
    */
    Long decr(String key, long delta);

    /**
     * @description: 获取Hash结构中的属性
     * @param key
     * @param hashKey
     * @return {@link Object}
     * @auther apecode
     * @date 2022/5/29 18:58
    */
    Object hGet(String key, String hashKey);

    /**
     * @description: 向Hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     * @param time
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * @description: 向Hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    void hSet(String key, String hashKey, Object value);

    /**
     * @description: 获取hash结构中所有属性
     * @param key
     * @return {@link Map<String,Object>}
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    Map<String, Object> hmGet(String key);

    /**
     * @description: 添加多个hash结构
     * @param key
     * @param value
     * @param time
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    Boolean hmSet(String key, Map<String, Object> value, long time);

    /**
     * @description: 添加多个hash结构
     * @param key
     * @param value
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    void hmSet(String key, Map<String, ?> value);

    /**
     * @description: 删除Hash结构中的属性
     * @param key
     * @param hashKeys
     * @auther apecode
     * @date 2022/5/29 18:59
    */
    void hDel(String key, Object... hashKeys);

    /**
     * @description: 判断Hash结构中是否有该属性
     * @param key
     * @param hashKey
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Boolean hHasKey(String key, String hashKey);

    /**
     * @description: Hash结构中属性递增
     * @param key
     * @param hashKey
     * @param delta
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * @description: Hash结构中属性递减
     * @param key
     * @param hashKey
     * @param delta
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * @description: 获取Hash结构长度
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2023/7/21 14:14
    */
    Long hSize(String key);

    /**
     * @description: 有序集合中数据递增
     * @param key
     * @param value
     * @param score
     * @return {@link Double}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Double zIncr(String key, Object value, Double score);

    /**
     * @description: 有序集合中数据递减
     * @param key
     * @param value
     * @param score
     * @return {@link Double}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Double zDecr(String key, Object value, Double score);

    /**
     * @description: 根据分数排名获取指定元素信息
     * @param key
     * @param start
     * @param end
     * @return {@link Map<Object,Double>}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Map<Object, Double> zReverseRangeWithScore(String key, long start, long end);

    /**
     * @description: 获取指定元素分数
     * @param key
     * @param value
     * @return {@link Double}
     * @auther apecode
     * @date 2022/5/29 19:00
    */
    Double zScore(String key, Object value);

    /**
     * @description: 获取所有分数
     * @param key
     * @return {@link Map<Object,Double>}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Map<Object, Double> zAllScore(String key);

    /**
     * @description: 删除指定Zset元素
     * @param key
     * @param value
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Long zRem(String key, Object... value);

    /**
     * @description: 获取Set结构
     * @param key
     * @return {@link Set<Object>}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Set<Object> sMembers(String key);

    /**
     * @description: 随机获取指定数量的Set
     * @param key
     * @param count
     * @return {@link List<Object>}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    List<Object> sRandMembers(String key, Long count);

    /**
     * @description: 随机获取Set
     * @param key
     * @return {@link Object}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Object sRandMember(String key);

    /**
     * @description: 获取不同的随机成员
     * @param key
     * @param count
     * @return {@link Set<Object>}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Set<Object> sDistinctRandomMembers(String key, Long count);

    /**
     * @description: 向Set结构中添加属性
     * @param key
     * @param values
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:01
    */
    Long sAdd(String key, Object... values);

    /**
     * @description: 向Set结构中添加属性
     * @param key
     * @param time
     * @param values
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long sAddExpire(String key, Long time, Object... values);

    /**
     * @description: 是否为Set中的属性
     * @param key
     * @param value
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Boolean sIsMember(String key, Object value);

    /**
     * @description: Set的长度
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long sSize(String key);

    /**
     * @description: 删除Set中的属性
     * @param key
     * @param values
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long sRemove(String key, Object... values);

    /**
     * @description: 删除Set中的属性
     * @param key
     * @param start
     * @param end
     * @return {@link List<Object>}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    List<Object> lRange(String key, long start, long end);

    /**
     * @description: 获取List中的长度
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long lSize(String key);

    /**
     * @description: 根据索引获取List中的属性
     * @param key
     * @param index
     * @return {@link Object}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Object lIndex(String key, long index);

    /**
     * @description: 向List中添加属性
     * @param key
     * @param value
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long lPush(String key, Object value);

    /**
     * @description: 向List中添加属性
     * @param key
     * @param value
     * @param time
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:02
    */
    Long lPush(String key, Object value, long time);

    /**
     * @description: 向List中批量添加属性
     * @param key
     * @param values
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Long lPushAll(String key, Object... values);

    /**
     * @description: 向List中批量添加属性
     * @param key
     * @param time
     * @param values
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * @description: 从List中移除属性
     * @param key
     * @param count
     * @param value
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Long lRemove(String key, long count, Object value);

    /**
     * @description: 向Bitmap中新增值
     * @param key
     * @param offset
     * @param b
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Boolean bitAdd(String key, int offset, boolean b);
    
    /**
     * @description: 从Bitmap中获取偏移量的值
     * @param key
     * @param offset
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Boolean bitGet(String key, int offset);

    /**
     * @description: 获取Bitmap的key值总和
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:03
    */
    Long bitCount(String key);

    /**
     * @description: 获取Bitmap范围值
     * @param key
     * @param limit
     * @param offset
     * @return {@link List<Long>}
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    List<Long> bitField(String key, int limit, int offset);

    /**
     * @description: 获取所有Bitmap
     * @param key
     * @return {@link byte}
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    byte[] bitGetAll(String key);

    /**
     * @description: 向hyperlog中添加数据
     * @param key
     * @param value
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    Long hyperAdd(String key, Object... value);

    /**
     * @description: 获取hyperlog元素数量
     * @param key
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    Long hyperGet(String... key);

    /**
     * @description: 删除hyperlog数据
     * @param key
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    void hyperDel(String key);

    /**
     * @description: 增加坐标
     * @param key
     * @param x
     * @param y
     * @param name
     * @return {@link Long}
     * @auther apecode
     * @date 2022/5/29 19:04
    */
    Long geoAdd(String key, Double x, Double y, String name);

    /**
     * @description: 根据城市名称获取坐标集合
     * @param key
     * @param place
     * @return {@link List<Point>}
     * @auther apecode
     * @date 2022/5/29 19:05
    */
    List<Point> geoGetPointList(String key, Object... place);

    /**
     * @description: 计算两个城市之间的距离
     * @param key
     * @param placeOne
     * @param placeTow
     * @return {@link Distance}
     * @auther apecode
     * @date 2022/5/29 19:05
    */
    Distance geoCalculationDistance(String key, String placeOne, String placeTow);

    /**
     * @description: 获取附该地点附近的其他地点
     * @param key
     * @param place
     * @param distance
     * @param limit
     * @param sort
     * @return {@link GeoResults<GeoLocation<Object>>}
     * @auther apecode
     * @date 2022/5/29 19:05
    */
    GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByPlace(String key, String place, Distance distance, long limit, Sort.Direction sort);

    /**
     * @description: 获取地点的hash
     * @param key
     * @param place
     * @return {@link List<String>}
     * @auther apecode
     * @date 2022/5/29 19:05
    */
    List<String> geoGetHash(String key, String... place);

}

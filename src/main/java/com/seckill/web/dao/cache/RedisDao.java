package com.seckill.web.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.web.model.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 使用redis缓存
 *
 * @author hulupeng
 * @date 2019/9/12 12:01
 */
public class RedisDao {
    //打印日志带RedisDao类的信息，不然不好查找日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;//类似于连接池

    public RedisDao(String ip, int prot) {  //jedisPool构造方法
        this.jedisPool = new JedisPool(ip, prot);
    }

    //使用protostuff实现序列化
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    public SecKill getSecKill(long secKillId) {
        //redis逻辑，从cache中获取secKill秒杀对象
        try {
            Jedis jedis = jedisPool.getResource();//这个redis相当于数据库的连接
            try {
                String key = "secKill:" + secKillId;
                //使用protostuff将SecKill对象序列化，存储到缓存在
                //取出数据时，将二进制数组反序列化得到该对象
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {//如果获取的字节码不为空
                    SecKill secKill = schema.newMessage();//根据schema创建空对象
                    ProtostuffIOUtil.mergeFrom(bytes, secKill, schema);//将信息赋值给SecKill对象，即反序列化
                    return secKill;
                }
            } finally {
                jedis.close();//jedis关闭
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    public String putSecKill(SecKill secKill){
        //如果对象不在缓存在，则将对象放入缓存中
        //将对象序列化，以字节数组缓存到redis中
        try{
            Jedis jedis=jedisPool.getResource();//从jedis缓存池中取出资源，就相当于连接redis
            logger.info("get redis success!");
            try {
                String key="secKill"+secKill.getSecKillId();
                byte [] bytes=ProtostuffIOUtil.toByteArray(secKill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout=60*60;      //缓存一个小时
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();//关闭jedis缓存
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}

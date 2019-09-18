package com.seckill.web.dao.cache;

import com.alibaba.fastjson.JSON;
import com.seckill.web.dao.SecKillDao;
import com.seckill.web.model.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//指明spring配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1L;//定义秒杀商品的id
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SecKillDao secKillDao;


    //测试redis中两种方法
    @Test
    public void getSecKill() throws Exception {
    //1、从redis中取数据
        SecKill secKill = redisDao.getSecKill(id);//通过id从redis中取数据，将二进制数组通过反序列化转化为对象
        if (secKill==null){     //没有取到数据
            secKill=secKillDao.queryById(id);   //从数据取数据
            System.out.println(JSON.toJSONString(secKill));
            if (secKill !=null){     //取到数据
                String result = redisDao.putSecKill(secKill);//将对象序列化，以字节数组缓存到redis中
                System.out.println("result====="+result);
                secKill=redisDao.getSecKill(id);    //再将数据从redis中取出来
                System.out.println("secKill==="+secKill);
            }
        }
    }
}
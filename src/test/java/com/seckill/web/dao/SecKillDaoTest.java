package com.seckill.web.dao;

import com.alibaba.fastjson.JSON;
import com.seckill.web.model.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置junit和spring整合，这样junit在启动时就会加载junit容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit，spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {

    //注入dao实现类的依赖
    @Resource
    private SecKillDao secKillDao;

    @Test
    public void queryById() throws Exception {
        long secKillId=1;
        SecKill secKill = secKillDao.queryById(secKillId);
        System.out.println("商品名称===="+secKill.getName());
        System.out.println(JSON.toJSONString(secKill));
    }

    @Test
    public void queryAll() throws Exception {
        List<SecKill> killList = secKillDao.queryAll(0, 4);
        System.out.println(JSON.toJSONString(killList));
    }

    @Test
    public void reduceNumber() throws Exception {
        long secKillId=1;
        int reduceNumber = secKillDao.reduceNumber(secKillId, new Date());
        System.out.println("减库存==="+reduceNumber);
    }
}
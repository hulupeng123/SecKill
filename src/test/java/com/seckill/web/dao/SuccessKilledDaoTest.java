package com.seckill.web.dao;

import com.alibaba.fastjson.JSON;
import com.seckill.web.model.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，这样junit在启动时就会加载spring容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long secKillId = 2L;
        long userPhone = 13212345678L;
        int insertCount = successKilledDao.insertSuccessKilled(secKillId, userPhone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithPhone() throws Exception {
        long secKillId=1L;
        long userPhone=13212345678L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithPhone(secKillId, userPhone);
        System.out.println(JSON.toJSONString(successKilled));
    }
}
package com.seckill.web.service;

import com.alibaba.fastjson.JSON;
import com.seckill.web.dao.SecKillDao;
import com.seckill.web.dto.Exposer;
import com.seckill.web.dto.SecKillExecution;
import com.seckill.web.exception.RepeatKillException;
import com.seckill.web.exception.SecKillCloseException;
import com.seckill.web.model.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SecKillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService seckillService;

    @Test
    public void getSecKillById() throws Exception {
        long id = 1L;
        SecKill seckill = seckillService.getSecKillById(id);
        System.out.println(JSON.toJSONString(seckill));
    }

    @Test
    public void getSecKillList() throws Exception {
        List<SecKill> secKillList = seckillService.getSecKillList();
        System.out.println(JSON.toJSONString(secKillList));
    }

    @Test
    // 完整的测试代码，即将exportSecKillUrl()方法与executeSecKill()方法结合起来测试
    public void SecKillLogic() throws Exception {
        long id = 1L;
        Exposer exposer = seckillService.exportSecKillUrl(id);
        if (exposer.isExposed()) {//如果现在是秒杀状态
            System.out.println(exposer);
            //logger.info("exposer={}",exposer);

            long userPhone = 13212345678L;
            String md5 = exposer.getMd5();
            try {
                SecKillExecution execution = seckillService.executeSecKill(id, userPhone, md5);
                System.out.println("执行秒杀操作返回结果"+execution);
                logger.info("execution={}",execution);
            } catch (SecKillCloseException e) {
                logger.error(e.getMessage(),e);
            }catch (RepeatKillException e){
                logger.error(e.getMessage(),e);
            }
        }else{//秒杀未开启
                logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void executeSecKillProcedure() throws Exception {
        long id=1L;
        long userPhone=13212345678L;
        Exposer exposer = seckillService.exportSecKillUrl(id);//判断秒杀是否开始
        if (exposer.isExposed()){//秒杀开始
            String md5=exposer.getMd5();
            SecKillExecution execution = seckillService.executeSecKillProcedure(id, userPhone, md5);
            logger.info("========================================"+execution.getStateInfo());

        }

    }
}
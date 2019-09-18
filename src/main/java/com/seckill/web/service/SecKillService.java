package com.seckill.web.service;

import com.seckill.web.dto.Exposer;
import com.seckill.web.dto.SecKillExecution;
import com.seckill.web.exception.RepeatKillException;
import com.seckill.web.exception.SecKillCloseException;
import com.seckill.web.exception.SecKillException;
import com.seckill.web.model.SecKill;

import java.util.List;

/**
 * 业务接口
 * 商品的库存、商品的参数、以及返回类型（在这期间可能会遇到异常：重复秒杀、秒杀已结束等异常）
 *
 * @author hulupeng
 * @date 2019/9/12 16:17
 */
public interface SecKillService {
    /**
     * 通过商品id返回id所对应的商品信息
     * @param secKillId
     * @return
     * @throws Exception
     */
    public SecKill getSecKillById(long secKillId) throws Exception;

    /**
     * 查询所有商品信息
     * @return
     * @throws Exception
     */
    public List<SecKill> getSecKillList()throws Exception;

    /**
     * 功能：秒杀开始时，输入秒杀接口地址，秒杀还未开始输出系统时间和秒杀开始时间（倒计时）
     * @param secKillId
     * @return
     * @throws Exception
     */
    public Exposer exportSecKillUrl(long secKillId)throws Exception;

    /**
     * 执行秒杀操作
     * @param secKillId  秒杀商品的id
     * @param userPhone  用户手机号
     * @param md5        md5加密
     * @return  返回秒杀成功或者失败信息
     * @throws SecKillException
     * @throws RepeatKillException
     * @throws SecKillCloseException
     */
    public SecKillExecution executeSecKill(long secKillId, long userPhone, String md5)throws Exception,SecKillException, RepeatKillException, SecKillCloseException;

    /**
     *通过sql包下的secKill.sql存储过程实现优化操作
     * @param secKillId     秒杀商品的id
     * @param userPhone     用户的手机号码
     * @param md5
     * @return
     * @throws Exception
     */
    public SecKillExecution executeSecKillProcedure(long secKillId, long userPhone, String md5)throws Exception;
}

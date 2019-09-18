package com.seckill.web.dao;

import com.seckill.web.model.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @author hulupeng
 * @date 2019/9/11 17:43
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细,可过滤重复
     * @param secKillId
     * @param userPhone
     * @return插入的行数
     */
    public int insertSuccessKilled(@Param("secKillId") long secKillId, @Param("userPhone") long userPhone)throws Exception;

    /**
     * 通过商品id和用户电话来查询是否重复秒杀或者秒杀结束
     * @param secKillId
     * @param userPhone
     * @return
     * @throws Exception
     */
    public SuccessKilled queryByIdWithPhone(@Param("secKillId") long secKillId,@Param("userPhone") long userPhone)throws Exception;
}

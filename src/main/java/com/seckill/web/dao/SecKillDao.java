package com.seckill.web.dao;

import com.seckill.web.model.SecKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hulupeng
 * @date 2019/9/11 17:29
 */
public interface SecKillDao {
    /**
     * 根据id查询秒杀商品
     * @param secKillId
     * @return
     */
    public SecKill queryById(long secKillId)throws Exception;

    /**
     * 查询全部商品信息
     * @return
     */
    public List<SecKill> queryAll(@Param("offset") int offset, @Param("limit") int limit)throws Exception;

    /**
     * 减库存
     * @param secKillId
     * @param killTime          表示秒杀时间，需要和开始时间startTime比较，当startTime<=killTime时，才可以秒杀
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    public int reduceNumber(@Param("secKillId") long secKillId, @Param("killTime") Date killTime)throws Exception;
    /**
     * 用存储过程来执行秒杀操作
     * @param map
     * @throws Exception
     */
    public void killByProcedure(Map<String,Object> map)throws Exception;
}

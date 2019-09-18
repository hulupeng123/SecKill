package com.seckill.web.service.impl;

import com.seckill.web.dao.SecKillDao;
import com.seckill.web.dao.SuccessKilledDao;
import com.seckill.web.dao.cache.RedisDao;
import com.seckill.web.dto.Exposer;
import com.seckill.web.dto.SecKillExecution;
import com.seckill.web.enums.SecKillStateEnum;
import com.seckill.web.exception.RepeatKillException;
import com.seckill.web.exception.SecKillCloseException;
import com.seckill.web.exception.SecKillException;
import com.seckill.web.model.SecKill;
import com.seckill.web.model.SuccessKilled;
import com.seckill.web.service.SecKillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现SecKillService接口
 *
 * @author hulupeng
 * @date 2019/9/16 9:07
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    //设置设置日志文件（slf4j文件）
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入service依赖
    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //使用MD5加密设置盐值（越复杂越好）
    private final String salt="jsisufdl8&&%%@22";
    /**
     * 通过商品id获得商品信息
     *
     * @param secKillId
     * @return
     * @throws Exception
     */
    @Override
    public SecKill getSecKillById(long secKillId) throws Exception {
        return secKillDao.queryById(secKillId);
    }

    /**
     * 展示所有秒杀商品
     * @return
     * @throws Exception
     */
    @Override
    public List<SecKill> getSecKillList() throws Exception {
        return secKillDao.queryAll(0,4);
    }

    /**
     * 需要判断下
     * @param secKillId     通过秒杀商品的id获取秒杀开始时间与系统时间进行比较
     * @return
     * @throws Exception
     */
    @Override
    public Exposer exportSecKillUrl(long secKillId) throws Exception {
        //缓存优化，超时基础上维护一致性
        SecKill secKill = redisDao.getSecKill(secKillId);   //从redis缓存中取数据
        if (secKill==null){     //如果缓存中没有数据，就从数据库中取
            secKill = secKillDao.queryById(secKillId);     //从数据库中取数据
            if (secKill==null){  //获取的秒杀商品为空
                return new Exposer(false,secKillId);  //不开启秒杀位，并将商品id传过去
            }else {   //将数据存储到redis中
                redisDao.putSecKill(secKill);
            }
        }
        Date startTime = secKill.getStartTime();    //获取商品秒杀的开始时间
        Date endTime = secKill.getEndTime();        //获取商品秒杀的结束时间
        Date now=new Date();                        //获取系统当前时间
        if (now.getTime() < startTime.getTime() || now.getTime()>endTime.getTime() ){
            //不在秒杀时间范围
            return new Exposer(false,secKillId,now.getTime(),startTime.getTime(),endTime.getTime());
        }
       String md5=getMd5(secKillId);
        return new Exposer(true,md5,secKillId);//秒杀开始
    }

    private String getMd5(long secKillId) {
        //使用md5加密，在exportSecKillUrl和executeSecKill都会使用到
        String base=secKillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());    // 通过org.springframework.util的一个工具类得到md5
        return md5;
    }

    /**使用存储过程实现添加秒杀成功数据和修改秒杀商品的库存操作返回结果状态
     * 并发优化
     * @param secKillId  秒杀商品的id
     * @param userPhone  用户手机号
     * @param md5        md5加密
     * @return
     * @throws SecKillException
     * @throws RepeatKillException
     * @throws SecKillCloseException
     */
    @Override
    @Transactional
    /**
     * 使用注解控制事务的优点
     * - 开发团队达成一致约定，明确表明事务方法的编程风格
     * - 保证事务方法的时间尽可能短，不要穿插其他网络操作（如RPC/HTTP请求），要将这些操作剥离到事务方法外部
     * - 不是所有方法都需要事务，如：只有一条添加/修改/删除操作，只读操作不要事务控制
     */
    public SecKillExecution executeSecKill(long secKillId, long userPhone, String md5) throws Exception ,SecKillException, RepeatKillException, SecKillCloseException {
        if (md5==null || !md5.equals(getMd5(secKillId))){   //如果MD5为空或者MD5值不等于秒杀商品id的MD5的值
            throw new SecKillException("secKill data rewrite"); //抛出相应的异常，秒杀数据重写
        }
        try {
            /**
             * 执行秒杀逻辑，也就是之前的减库存，记录购买行为
             * 这里可以优化下，先增加在修改，这样就减少行级锁的执行次数(重复秒杀不会执行update，这样就减少行级锁执行次数)
             *缩短执行时间
             */
            int insertCount = successKilledDao.insertSuccessKilled(secKillId, userPhone);//插入数据
            if (insertCount<=0){
                //表示该用户已经秒杀,不能再秒杀
                throw new RepeatKillException("secKill repeated");
            }else {
                int updateCount = secKillDao.reduceNumber(secKillId, new Date());//减库存
                if (updateCount<=0){
                    //没有更新到记录中，秒杀已结束
                    throw new SecKillCloseException("secKill is closed");
                }else{//秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithPhone(secKillId, userPhone);
                    return new SecKillExecution(secKillId, SecKillStateEnum.SUCCESS, successKilled);
                }
            }
        }catch (SecKillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //将编译期异常转化为运行期异常
            throw new SecKillException("secKill inner error"+e.getMessage());
        }
    }

    /**
     * 调用存储过程实现秒杀业务逻辑,深度优化
     * @param secKillId     秒杀商品的id
     * @param userPhone     用户的手机号码
     * @param md5
     * @return
     * @throws Exception
     */
    @Override
    public SecKillExecution executeSecKillProcedure(long secKillId, long userPhone, String md5) throws Exception {
        if (md5==null || !md5.equals(getMd5(secKillId))){
            return new SecKillExecution(secKillId,SecKillStateEnum.DATA_REWRITE);
        }
        Date killTime=new Date();  //秒杀时间
        Map<String,Object> map = new HashMap<>();
        map.put("secKillId",secKillId);
        map.put("userPhone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            //使用存储过程，使用map可以比较方便的保存和获取result返回结果
            secKillDao.killByProcedure(map);
            //使用MapUtils(需导入common-collections包)获取result值
            //当result为null时可以默认赋值为-2，系统错误
            Integer result = MapUtils.getInteger(map, "result", -2);
            if (result==1){//秒杀成功
                SuccessKilled successKilled = successKilledDao.queryByIdWithPhone(secKillId, userPhone);
                return new SecKillExecution(secKillId,SecKillStateEnum.SUCCESS,successKilled);
            }else{  //秒杀失败，返回result结果及返回对应的状态
                System.out.println("result===="+result);
                System.out.println("秒杀的状态===="+SecKillStateEnum.stateOf(result));
                return new SecKillExecution(result,SecKillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SecKillExecution(secKillId,SecKillStateEnum.INNER_ERROR);//系统内部错误
        }
    }
}

package com.seckill.web.dto;

import com.seckill.web.enums.SecKillStateEnum;
import com.seckill.web.model.SuccessKilled;

/**
 * 封装秒杀执行结果，给SecKillService和ExecuteSecKill调用
 * @author hulupeng
 * @date 2019/9/12 16:32
 */
public class SecKillExecution {
    private long secKillId;             //秒杀商品的id
    private int state;                  //秒杀商品的状态（状态标识:-1:无效 0:成功 1:已付款 2:已发货）
    private String stateInfo;           //秒杀商品的状态信息
    private SuccessKilled successKilled;//秒杀成后需要返回秒杀成功信息（商品id、用户手机号等信息）

    //成功返回的数据
    public SecKillExecution(long secKillId, SecKillStateEnum secKillStateEnum, SuccessKilled successKilled) {
        this.secKillId = secKillId;
        this.state = secKillStateEnum.getState();
        this.stateInfo = secKillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }
    //失败返回的相应信息

    public SecKillExecution(long secKillId,SecKillStateEnum secKillStateEnum) {
        this.secKillId = secKillId;
        this.state = secKillStateEnum.getState();
        this.stateInfo = secKillStateEnum.getStateInfo();
    }

    public long getSecKillId() {
        return secKillId;
    }

    public void setSecKillId(long secKillId) {
        this.secKillId = secKillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SecKillExecution{" +
                "secKillId=" + secKillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}

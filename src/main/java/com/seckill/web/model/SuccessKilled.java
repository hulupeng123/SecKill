package com.seckill.web.model;

import java.util.Date;

/**
 * 编写秒杀成功的表所对应的实体类对象
 * @author hulupeng
 * @date 2019/9/11 17:25
 */
public class SuccessKilled {
    private long secKillId;         // 商品id
    private long userPhone;         // 用户手机号
    private short state;            // 状态标志：无效（-1），成功（0），已付款（1），已发货（2）
    private Date createTime;        // 创建时间
    //多对一,因为一件商品在库存中有很多数量，对应的购买明细也有很多。
    private SecKill secKill;
    public long getSecKillId() {
        return secKillId;
    }

    public void setSecKillId(long secKillId) {
        this.secKillId = secKillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "secKillId=" + secKillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", secKill=" + secKill +
                '}';
    }
}

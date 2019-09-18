package com.seckill.web.model;

import java.util.Date;

/**
 * 编写secKill对应的实体类对象
 * @author hulupeng
 * @date 2019/9/11 17:23
 */
public class SecKill {
    private long secKillId;      // 秒杀商品id
    private String name;        // 秒杀商品名称
    private int number;         // 商品数量
    private Date startTime;     // 秒杀开始时间
    private Date endTime;       // 秒杀结束时间
    private Date createTime;    // 创建时间

    public long getSecKillId() {
        return secKillId;
    }

    public void setSecKillId(long secKillId) {
        this.secKillId = secKillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

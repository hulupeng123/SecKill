package com.seckill.web.dto;

/**
 *暴露秒杀的url的DTO（数据传输层）
 * Exposer中的数据与业务没有多大的关系，提供给SecKillService中ExposerSecKillUrl接口的调用
 * @author hulupeng
 * @date 2019/9/12 16:20
 */
public class Exposer {
    private boolean exposed;  //表示开启秒杀标志位置
    private String md5;       //用MD5算法加密
    private long secKillId;   //表示秒杀商品的id
    private long now;         //表示秒杀的时间
    private long start;       //秒杀开始时间
    private long end;         //秒杀结束时间

    public Exposer(boolean exposed, String md5, long secKillId, long now, long start, long end) {
        this.exposed = exposed;
        this.md5 = md5;
        this.secKillId = secKillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, String md5, long secKillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.secKillId = secKillId;
    }

    public Exposer(boolean exposed, long secKillId) {
        this.exposed = exposed;
        this.secKillId = secKillId;
    }

    public Exposer(boolean exposed, long secKillId, long now, long start, long end) {
        this.exposed = exposed;
        this.secKillId = secKillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer() {
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSecKillId() {
        return secKillId;
    }

    public void setSecKillId(long secKillId) {
        this.secKillId = secKillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}

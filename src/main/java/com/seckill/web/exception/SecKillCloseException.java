package com.seckill.web.exception;

/**
 * 当库存为0或者秒杀时间结束，秒杀已关闭用户无法进行秒杀操作
 * @author hulupeng
 * @date 2019/9/12 17:12
 */
public class SecKillCloseException extends SecKillException {
    public SecKillCloseException(String message) {
        super(message);
    }

    public SecKillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}

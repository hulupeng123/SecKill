package com.seckill.web.exception;

/**
 * 秒杀业务相关的异常
 * @author hulupeng
 * @date 2019/9/12 17:12
 */
public class SecKillException extends RuntimeException {

    public SecKillException(String message){super(message);}

    public SecKillException(String message,Throwable cause){super(message,cause);}
}

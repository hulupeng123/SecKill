package com.seckill.web.exception;

/**
 *重复秒杀（运行期异常）
 * @author hulupeng
 * @date 2019/9/12 17:11
 */
public class RepeatKillException extends SecKillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}

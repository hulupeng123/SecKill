package com.seckill.web.dto;

/**
 * 将所有的ajax返回类型都封装成json数据
 * @author hulupeng
 * @date 2019/9/12 17:05
 */
public class SecKillResult<T> {
    private boolean success;    //成功时返回的标志
    private T data;             //成功时返回的数据
    private String error;       //失败时返回的错误信息

    public SecKillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SecKillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

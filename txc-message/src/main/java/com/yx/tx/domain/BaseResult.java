package com.yx.tx.domain;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 2:00 PM
 *
 * 返回体
 */
public class BaseResult<T> {
    private int code;
    private T data;

    public BaseResult(int code) {
        this.code = code;
        this.data = null;
    }

    public static BaseResult getInstance(int code) {
        return new BaseResult(code);
    }

}

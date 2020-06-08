package com.yx.tx.producer;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 12:18 PM
 *
 *  事件发布接口
 */
public interface EventPublish {

    void start();

    void shutdown();
}

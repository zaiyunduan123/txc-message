package com.yx.tx.constant;


/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:19 AM
 *
 * 事件状态
 */
public enum  EventStatus {
    /**初始化*/
    PRODUCE_INIT,
    /**处理中*/
    PRODUCE_PROCESSING,
    /**完成*/
    PRODUCE_PROCESSED,

    /**发布后初始化*/
    CONSUME_INIT,
    /**发布后处理中*/
    CONSUME_PROCESSING,
    /**发布后处理完成*/
    CONSUME_PROCESSED,
    /**达到最大重试次数*/
    CONSUME_MAX_RECONSUMETIMES;
}

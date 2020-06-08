package com.yx.tx.scheduler;

import com.yx.tx.domain.TxEvent;

/**
 * 抽象消息发送调度
 */
public abstract class AbstractMessageScheduler {

    /**
     * 发送前
     */
    public abstract void processBeforeSendMessage(TxEvent txEvent);

    /**
     * 发送中
     */
    public abstract void sendMessage(TxEvent txEvent);


    /**
     * 发送后
     */
    public abstract void processAfterSendMessage(TxEvent txEvent);
}

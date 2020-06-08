package com.yx.tx.scheduler;

import com.yx.tx.constant.EventStatus;
import com.yx.tx.domain.TxEvent;
import com.yx.tx.producer.TxMessageProducerClient;
import com.yx.tx.service.BaseEventService;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/8 9:36 PM
 * <p>
 * 发送事务消息调度线程
 */
@Data
public class SendTxMessageScheduler extends AbstractMessageScheduler implements Runnable {

    ScheduledExecutorService scheduledExecutorService;

    TxMessageProducerClient txMessageProducerClient;

    // 初始化延时
    private long initialDelay = 0;
    // 两次开始执行最小间隔时间
    private long period = 5;
    // 核心线程数
    private int corePoolSize = 1;

    BaseEventService baseEventService;

    public SendTxMessageScheduler(int initialDelay,
                                  int period,
                                  int corePoolSize) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.corePoolSize = corePoolSize;
        scheduledExecutorService = Executors.newScheduledThreadPool(this.corePoolSize);

    }
    public SendTxMessageScheduler(){

    }

    @Override
    public void run() {
        // 查询并发消息
        try {
            List<TxEvent> txEvents = baseEventService.queryEventListByStatus(EventStatus.PRODUCE_INIT.toString());
            if (CollectionUtils.isEmpty(txEvents)) {
                return;
            }
            for (TxEvent txEvent : txEvents) {
                // 发送前改状态
                processBeforeSendMessage(txEvent);
                // 发送消息核心逻辑
                sendMessage(txEvent);
                // 判断发送结果，成功则更新为已发送
                processBeforeSendMessage(txEvent);
            }
        } catch (Exception e) {
            return;
        }
    }


    @Override
    public void processBeforeSendMessage(TxEvent txEvent) {

    }

    /**
     * 发送事务消息
     *
     * @param txEvent
     */
    @Override
    public void sendMessage(TxEvent txEvent) {

    }

    @Override
    public void processAfterSendMessage(TxEvent txEvent) {

    }

    public SendTxMessageScheduler setTxMessageProducerClient(TxMessageProducerClient txMessageProducerClient) {
        this.txMessageProducerClient = txMessageProducerClient;
        return this;
    }
}

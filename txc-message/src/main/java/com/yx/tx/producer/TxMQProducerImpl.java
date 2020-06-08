package com.yx.tx.producer;

import com.yx.tx.constant.CommonProperty;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 12:19 PM
 * <p>
 * 事件客户端核心实现
 */
@Component
public class TxMQProducerImpl implements EventPublish {

    //事务提交生产者
    @Autowired
    private DefaultMQProducer commitProducer;

    // 事务回滚生产者
    @Autowired
    private DefaultMQProducer rollbackProducer;

    public DefaultMQProducer getCommitProducer() {
        return commitProducer;
    }

    public void setCommitProducer(DefaultMQProducer commitProducer) {
        this.commitProducer = commitProducer;
    }

    public DefaultMQProducer getRollbackProducer() {
        return rollbackProducer;
    }

    public void setRollbackProducer(DefaultMQProducer rollbackProducer) {
        this.rollbackProducer = rollbackProducer;
    }

    public TxMQProducerImpl(String topic, String nameSrvAddr, int retryTimesWhenSendFailed) {
        commitProducer = new DefaultMQProducer();
        rollbackProducer = new DefaultMQProducer();
        initCommit(topic, nameSrvAddr, retryTimesWhenSendFailed);
        initRollback(topic, nameSrvAddr, retryTimesWhenSendFailed);
    }

    /**
     * 初始化事件提交发布客户端
     *
     * @param topic
     * @param nameSrvAddr
     * @param retryTimesWhenSendFailed
     * @return
     */
    TxMQProducerImpl initCommit(String topic, String nameSrvAddr, int retryTimesWhenSendFailed) {
        return this.init(this.commitProducer, topic, nameSrvAddr, CommonProperty.TRANSACTION_COMMMIT_STAGE, retryTimesWhenSendFailed);
    }

    /**
     * 初始化事件回滚发布客户端
     *
     * @param topic
     * @param nameSrvAddr
     * @param retryTimesWhenSendFailed
     * @return
     */
    TxMQProducerImpl initRollback(String topic, String nameSrvAddr, int retryTimesWhenSendFailed) {
        return this.init(this.rollbackProducer, topic, nameSrvAddr, CommonProperty.TRANSACTION_ROLLBACK_STAGE,
                retryTimesWhenSendFailed);
    }

    TxMQProducerImpl init(DefaultMQProducer defaultMQProducer,
                          String topic,
                          String nameSrvAddr,
                          String tranStage,
                          int retryTimesWhenSendFailed) {
        String producerGroup = new StringBuilder("GID_")
                .append(tranStage)
                .append(topic.toUpperCase())
                .toString();
        defaultMQProducer.setProducerGroup(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameSrvAddr);
        defaultMQProducer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        return this;
    }

    @Override
    public void start() {
        try {
            this.commitProducer.start();
            this.rollbackProducer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        this.commitProducer.shutdown();
        this.rollbackProducer.shutdown();
    }
}

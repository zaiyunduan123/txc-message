package com.yx.tx.producer;


import com.yx.tx.constant.EventStatus;
import com.yx.tx.constant.EventType;
import com.yx.tx.constant.TxType;
import com.yx.tx.domain.BaseResult;
import com.yx.tx.domain.TxEvent;
import com.yx.tx.domain.TxMessage;
import com.yx.tx.service.BaseEventService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:22 AM
 * 消息发布客户端
 */
public class TxMessageProducerClient implements EventPublish {

    /**
     * 发送消息成功
     */
    public static final int SEND_MESSAGE_SUCC = 0;

    /**
     * 发送消息失败
     */
    public static final int SEND_MESSAGE_FAIL = 1;

    @Autowired
    private TxMQProducerImpl txMQProducer;

    /**
     * 事件持久化
     */
    @Autowired
    private BaseEventService baseEventService;

    /**
     * 订阅主题
     */
    private String topic;

    /**
     * 集群
     */
    private String nameSrvAddr;

    /**
     * 失败后重试次数
     */
    private int retryTimesWhenSendFailed = 0;


    public TxMessageProducerClient(String topic, String nameSrvAddr, int retryTimesWhenSendFailed) {
        // 初始化commitProducer
        this.topic = topic;
        this.nameSrvAddr = nameSrvAddr;
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        txMQProducer = new TxMQProducerImpl(topic, nameSrvAddr, retryTimesWhenSendFailed);
        // 启动内部消息发送器
        txMQProducer.start();
    }


    /**
     * 消息持久化
     *
     * @param txMessage
     * @param eventType
     * @param txType
     * @param appId
     * @param bizKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(TxMessage txMessage, EventType eventType, TxType txType, String appId,
                            String bizKey) {

        TxEvent event = new TxEvent();

        event.setEventType(eventType.toString());
        event.setTxType(txType.toString());
        event.setEventStatus(EventStatus.PRODUCE_INIT.toString());
        event.setContent(txMessage.toString());
        event.setEventType(eventType.toString());
        event.setAppId(appId);
        event.setBizKey(bizKey);


        try {
            // 入库，持久化
            Integer result = baseEventService.insertEvent(event);
            if (result != 1) {
                // 入库失败就抛异常，让事务回滚
                throw new RuntimeException("insert failed!!!");
            }
        } catch (Exception e) {
            // 异常回滚
            throw new RuntimeException("insert failed!!!", e);
        }

    }

    /**
     * 发送提交的消息
     *
     * @param msg
     * @param eventId
     * @return
     */
    public BaseResult sendCommitMessage(Message msg, int eventId) {
        try {
            msg.setTopic(msg.getTopic());
            SendResult sendResult = this.txMQProducer.getCommitProducer().send(msg);
            return processAfterSendMsg(sendResult, eventId);
        } catch (Exception e) {
            return BaseResult.getInstance(SEND_MESSAGE_FAIL);
        }

    }

    /**
     * 发送消息后处理逻辑
     *
     * @param sendResult
     * @return
     */
    private BaseResult processAfterSendMsg(SendResult sendResult, int eventId) {
        if (sendResult == null || StringUtils.isBlank(sendResult.getMsgId())){
            return BaseResult.getInstance(SEND_MESSAGE_FAIL);
        }
        // 更新消息状态:处理中->完成
        TxEvent event = new TxEvent();
        event.setId(eventId);
        event.setEventStatus(EventStatus.PRODUCE_PROCESSED.toString());
        event.setBeforeUpdateEventStatus(EventStatus.PRODUCE_PROCESSING.toString());
        this.baseEventService.updateEventStatusById(event);
        return BaseResult.getInstance(SEND_MESSAGE_SUCC);
    }

        @Override
        public void start () {
            this.txMQProducer.start();
        }

        @Override
        public void shutdown () {
            this.txMQProducer.shutdown();
        }
    }

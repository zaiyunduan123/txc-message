package com.yx.tx.listener;

import com.yx.tx.constant.CommonProperty;
import com.yx.tx.domain.TxEvent;
import com.yx.tx.domain.TxMessage;
import com.yx.tx.service.BaseEventService;
import com.yx.tx.util.SpringApplicationHolder;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.logging.Logger;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 2:52 PM
 * <p>
 * 事务提交消息监听器
 */
public class TxCommitListener implements MessageListenerConcurrently {

    /**
     * 拉取到消息直接消费
     *
     * @param list
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        for (MessageExt msg : list) {
            String msgBody = new String(msg.getBody());
            String msgId = msg.getMsgId();

            TxMessage txMessage = new TxMessage();
            txMessage.decode(msgBody);

            TxEvent txEvent = new TxEvent();
            txEvent.convert(txMessage);

            BaseEventService baseEventService =
                    (BaseEventService) SpringApplicationHolder.getBean("baseEventService");
        }
        return null;
    }
}

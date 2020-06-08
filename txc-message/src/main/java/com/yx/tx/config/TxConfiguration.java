package com.yx.tx.config;

import com.yx.tx.scheduler.SendTxMessageScheduler;
import com.yx.tx.service.BaseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/8 9:31 PM
 *
 *  内部维护一个定时任务，扫描状态为 初始化待发送 消息，组装成功消息体并调用MQ的发送消息接口，将投递到 事务提交队列。这个过程中要保证消息可达。
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)//开启可配置能力
public class TxConfiguration {


    private BaseEventService baseEventService;

    /**
     * 异步消息调度构造
     * @param rocketMQProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @Order(3)
    public SendTxMessageScheduler sendTxMessageScheduler(RocketMQProperties rocketMQProperties){
        SendTxMessageScheduler sendTxMessageScheduler = new SendTxMessageScheduler();
        // 设置调度线程池参数
        sendTxMessageScheduler.setInitialDelay(rocketMQProperties.getTranMessageSendInitialDelay());
        sendTxMessageScheduler.setPeriod(rocketMQProperties.getTranMessageSendPeriod());
        sendTxMessageScheduler.setCorePoolSize(rocketMQProperties.getTranMessageSendCorePoolSize());
        // 数据库操作
        sendTxMessageScheduler.setBaseEventService(baseEventService);
        // 消息发送
        sendTxMessageScheduler.setTxMessageProducerClient(null);
        return null;
    }




}

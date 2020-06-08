package com.yx.tx.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:36 AM
 *
 * 基础事件
 */
@Data
public class TxEvent {

    private Boolean success = Boolean.FALSE;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 事务类型
     */
    private String txType;
    /**
     * 事件状态
     */
    private String eventStatus;
    /**
     * 业务实体
     */
    private String content;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 记录状态
     */
    private int recordStatus;
    /**
     * 业务键
     */
    private String bizKey;
    /**
     * 更新前状态
     */
    private String beforeUpdateEventStatus;
    /**
     * 创建时间
     */
    private Date Created;
    /**
     * 更新时间
     */
    private Date Updated;

    /**
     * 转换消息协议为事件
     * @param txMessage
     */
    public void convert(TxMessage txMessage) {
        this.setId(Integer.valueOf(txMessage.getId()));
        this.setEventType(txMessage.getEventType());
        this.setTxType(txMessage.getTxType());
        this.setEventStatus(txMessage.getEventStatus());
        this.setContent(txMessage.getContent());
        this.setAppId(txMessage.getAppId());
        this.setBizKey(txMessage.getBizKey());
    }
}

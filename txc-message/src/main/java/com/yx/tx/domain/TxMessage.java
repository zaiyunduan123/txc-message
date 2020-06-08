package com.yx.tx.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.io.IOException;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:31 AM
 * <p>
 * 事务消息
 */
@Data
public class TxMessage {

    private String name;
    /**
     * 自增主键
     */
    private String id;
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
     * 业务键
     */
    private String bizKey;

    public TxMessage(String name) {
        this.name = name;
    }

    public TxMessage() {

    }

    public String encode() {

        ImmutableMap<String, Object> messageBody = new ImmutableMap.Builder<String, Object>()
                .put("id", String.valueOf(this.getId()))
                .put("eventType", this.getEventType())
                .put("txType", this.getTxType())
                .put("eventStatus", this.getEventStatus())
                .put("content", this.getContent())
                .put("appId", this.getAppId())
                .put("bizKey", this.getBizKey())
                .build();

        String ret_string = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ret_string = objectMapper.writeValueAsString(messageBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RollbackMessage消息序列化json异常", e);
        }
        return ret_string;
    }

    public void decode(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(msg);
            this.setId(root.get("id").asText());
            this.setEventType(root.get("eventType").asText());
            this.setTxType(root.get("txType").asText());
            this.setEventStatus(root.get("eventStatus").asText());
            this.setContent(root.get("content").asText());
            this.setAppId(root.get("appId").asText());
            this.setBizKey(root.get("bizKey").asText());
        } catch (IOException e) {
            throw new RuntimeException("NineYiPriceQueryTaskProtocol反序列化消息异常", e);
        }
    }
}

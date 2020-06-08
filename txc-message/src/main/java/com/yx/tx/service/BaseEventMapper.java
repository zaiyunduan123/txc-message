package com.yx.tx.service;

import com.yx.tx.domain.TxEvent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:58 AM
 * <p>
 * 事件持久 dao层
 */
@Mapper
public interface BaseEventMapper {

    int insert(TxEvent event);

    int update(TxEvent event);

    void delete(TxEvent event);

    int updateStatus(TxEvent event);

    int updateEventStatusById(TxEvent event);

    /**
     * 根据事件状态获取事件列表
     * @param eventStatus
     * @return
     */
    List<TxEvent> queryEventListByStatus(String eventStatus);
}

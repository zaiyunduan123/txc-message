package com.yx.tx.service;

import com.yx.tx.domain.TxEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 9:48 AM
 * 事件持久 service层
 */
@Service
public class BaseEventService {

    @Autowired
    private BaseEventMapper baseEventMapper;


    /**
     * 插入事件
     *
     * @param event
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer insertEvent(TxEvent event) {
        return baseEventMapper.insert(event);
    }

    public Integer updateEventStatusById(TxEvent event) {
        return baseEventMapper.updateEventStatusById(event);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<TxEvent> queryEventListByStatus(String status) {
        return baseEventMapper.queryEventListByStatus(status);
    }
}

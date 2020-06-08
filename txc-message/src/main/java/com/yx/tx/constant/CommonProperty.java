package com.yx.tx.constant;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 1:46 PM
 */
public class CommonProperty {

    private CommonProperty() {}

    /**事务提交阶段*/
    public static final String TRANSACTION_COMMMIT_STAGE = "TX_COMMIT_";

    /**事务回滚阶段*/
    public static final String TRANSACTION_ROLLBACK_STAGE = "TX_ROLLBACK_";

    /**默认topic源*/
    public static final String DEFAULT_TXC_TOPIC = "DEFAULT_TX_XXX";

    /**事务下游执行提交最大次数*/
    public static final int MAX_COMMIT_RECONSUME_TIMES = 3;

    /**消息存在*/
    public static final String MESSAGE_HAS_EXISTED_INDEX = "message_exists_idx";

    /**消息存在*/
    public static final String MESSAGE_PRIMARY_KEY_DUPLICATE = "PRIMARY";
}

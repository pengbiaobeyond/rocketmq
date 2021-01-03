package com.mayikt.transaction;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.entity.OrderEntity;
import com.mayikt.mapper.OrderMapper;
import com.mayikt.utils.TransationalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: SyncProducerListener
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/221:42
 */
@Slf4j
@Component
@RocketMQTransactionListener(txProducerGroup = "mayiktProducer")
public class SyncProducerListener implements RocketMQLocalTransactionListener {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TransationalUtils transationalUtils;

    /**
     * 执行我们订单的事务
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        MessageHeaders headers = msg.getHeaders();
        Object object = headers.get("msg");
        if (object == null) {
            return null;
        }
        String orderMsg = (String) object;
        OrderEntity orderEntity = JSONObject.parseObject(orderMsg, OrderEntity.class);
        TransactionStatus begin = null;
        try {
            begin = transationalUtils.begin();
            int result = orderMapper.addOrder(orderEntity);
            transationalUtils.commit(begin);
            if (result <= 0) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
//            // 告诉我们的Broke可以消费者该消息
//            return RocketMQLocalTransactionState.COMMIT;
            return null;
        } catch (Exception e) {
            if (begin != null) {
                transationalUtils.rollback(begin);
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        }
        //add.Order
        return null;
    }

    /**
     * 提供给我们的Broker定时检查
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        MessageHeaders headers = msg.getHeaders();
        Object object = headers.get("msg");
        if (object == null) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        String orderMsg = (String) object;
        OrderEntity orderEntity = JSONObject.parseObject(orderMsg, OrderEntity.class);
        String orderId = orderEntity.getOrderId();
        // 直接查询我们的数据库
        OrderEntity orderDbEntity = orderMapper.findOrderId(orderId);
        if (orderDbEntity == null) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }
}

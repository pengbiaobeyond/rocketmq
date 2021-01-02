package com.mayikt.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "pengbiaoone", consumerGroup = "pengbiaoone", consumeMode
        = ConsumeMode.ORDERLY, consumeThreadMax = 1
)
public class OrdeConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println(Thread.currentThread().getName() + "," +
                "队列" + message.getQueueId() + "," + new String(message.getBody()));
    }
}

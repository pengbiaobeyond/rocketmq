package com.mayikt.producer;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/sendMsg")
    public String sendMsg() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Long orderId = System.currentTimeMillis();
        String insertSql = getSqlMsg("insert", orderId);
        String updateSql = getSqlMsg("update", orderId);
        String deleteSql = getSqlMsg("delete", orderId);
        Message insertMsg = new Message("pengbiaoone", insertSql.getBytes());
        Message updateMsg = new Message("pengbiaoone", updateSql.getBytes());
        Message deleteMsg = new Message("pengbiaoone", deleteSql.getBytes());
        DefaultMQProducer producer = rocketMQTemplate.getProducer();

        rocketMQTemplate.getProducer().send(insertMsg
                , new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg,
                                               Object arg) {
                        System.out.println("size================>>"+mqs.size());
                        Long orderId = (Long) arg;
                        long index = orderId % mqs.size();
                        return mqs.get((int) index);
                    }
                }, orderId);
        rocketMQTemplate.getProducer().send(updateMsg
                , new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg,
                                               Object arg) {
                        Long orderId = (Long) arg;
                        long index = orderId % mqs.size();
                        return mqs.get((int) index);

                    }
                }, orderId);
        rocketMQTemplate.getProducer().send(deleteMsg
                , new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg,
                                               Object arg) {
                        Long orderId = (Long) arg;
                        long index = orderId % mqs.size();
                        return mqs.get((int) index);
                    }
                }, orderId);

        return orderId + "";
    }

    public String getSqlMsg(String type, Long orderId) {
        JSONObject dataObject = new JSONObject();
        dataObject.put("type", type);
        dataObject.put("orderId", orderId);
        return dataObject.toJSONString();
    }


}

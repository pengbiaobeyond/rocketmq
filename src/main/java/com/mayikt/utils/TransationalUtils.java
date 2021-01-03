package com.mayikt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: TransationalUtils
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/218:43
 */
@Service
public class TransationalUtils {
    @Autowired
    public DataSourceTransactionManager transactionManager;

    public TransactionStatus begin() {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        return transaction;
    }

    public void commit(TransactionStatus transaction) {
        transactionManager.commit(transaction);

    }

    public void rollback(TransactionStatus transaction) {
        transactionManager.rollback(transaction);
    }

}

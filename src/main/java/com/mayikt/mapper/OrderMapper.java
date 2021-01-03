package com.mayikt.mapper;

import com.mayikt.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper {

    @Insert(value = "INSERT INTO `order_info` VALUES (#{id}, " +
            "#{name}, #{orderCreatetime}, #{orderMoney}, #{orderState}, #{commodityId},#{orderId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addOrder(OrderEntity orderEntity);


    @Select("SELECT * from order_info where orderId=#{orderId};")
    public OrderEntity findOrderId(@Param("orderId") String orderId);

}

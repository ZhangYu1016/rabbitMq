package com.producter;

import com.constant.VmConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.util.GetConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicRabbitProducter {

    private static final String exchange = "test-topic-exchange";
    private static final String key = "test.topic.key";

    public static void main(String[] args) throws IOException, TimeoutException {
        //2.创建连接
        Connection connection;
        if (!GetConnection.getConnection().isPresent())
            return;
        connection = GetConnection.getConnection().get();
        //3.创建通道
        Channel channel = connection.createChannel();
        //4.声明交换机
        channel.exchangeDeclare(exchange,"topic", false, true, null);
        //5.声明队列
//        channel.queueDeclare(queue, false, false, true, null);
        //6.交换机绑定队列，并定义路由key
//        channel.queueBind(queue, exchange, key);
        //7.发布消息
        channel.basicPublish(exchange, key, null, "topic第一个消息".getBytes());
        System.out.println("消息发布成功...");
        channel.close();
        connection.close();

    }
}

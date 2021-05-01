package com.producter;

import com.constant.VmConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.util.GetConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectRabbitProducter {

    private static final String exchange = "test-direct-exchange";
    private static final String queue = "test-direct-queue";
    private static final String key = "test-direct-key";

    public static void main(String[] args) throws IOException, TimeoutException {

        //2.创建连接
        Connection connection = null;
        if (!GetConnection.getConnection().isPresent())
            return;
        connection = GetConnection.getConnection().get();
        //3.创建通道
        Channel channel = connection.createChannel();
        //每次只向消费者发送一条消息,消费者使用后,手动确认后,才会发送另外一条
        channel.basicQos(0, 1, false);
        //4.声明交换机
        channel.exchangeDeclare(exchange,"direct", false, true, null);
        //5.声明队列
        channel.queueDeclare(queue, false, false, true, null);
        //6.交换机绑定队列，并定义路由key
        channel.queueBind(queue, exchange, key);
        //7.发布消息
        for (int i=0; i<5; i++) {
            String msg = "第"+i+"个消息";
            channel.basicPublish(exchange, key, null, msg.getBytes());
        }
        System.out.println("消息发布成功...");
        channel.close();
        connection.close();

    }
}

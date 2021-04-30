package com.producter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectRabbitProducter {

    private static final String host = "192.168.140.2";
    private static final int port = 5672;
    private static final String username = "guest";
    private static final String password = "guest";
    private static final String visualHost = "/";

    private static final String exchange = "test-direct-exchange";
    private static final String queue = "test-direct-queue";
    private static final String key = "test-direct-key";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(visualHost);

        //2.创建连接
        Connection connection = connectionFactory.newConnection();
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
        channel.basicPublish(exchange, key, null, "第一个消息".getBytes());
        System.out.println("消息发布成功...");
        channel.close();
        connection.close();

    }
}

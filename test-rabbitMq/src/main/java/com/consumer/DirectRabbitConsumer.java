package com.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectRabbitConsumer {

    private static final String host = "192.168.140.2";
    private static final int port = 5672;
    private static final String username = "guest";
    private static final String password = "guest";
    private static final String visualHost = "/";

    private static final String exchange = "test-direct-exchange";
    private static final String queue = "test-direct-queue";
    private static final String key = "test-direct-key";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

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
        //4.声明交换机
        channel.exchangeDeclare(exchange,"direct", false, true, null);
        //5.声明队列
        channel.queueDeclare(queue, false, false, true, null);
        //6.交换机绑定队列，并定义路由key
        channel.queueBind(queue, exchange, key);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        //7.发布消息
        channel.basicConsume(queue, true, consumer);

        while(true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String reciverMessage = new String(delivery.getBody());
            System.out.println("消费消息:-----"+reciverMessage);
        }
    }
}

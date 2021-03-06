package com.consumer;

import com.constant.VmConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.util.GetConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectRabbitConsumer {

    private static final String exchange = "test-direct-exchange";
    private static final String queue = "test-direct-queue";
    private static final String key = "test-direct-key";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //2.创建连接
        Connection connection;
        if (!GetConnection.getConnection().isPresent())
            return;
        connection = GetConnection.getConnection().get();
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
        channel.basicConsume(queue, false, consumer);

        while(true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String reciverMessage = new String(delivery.getBody());
            System.out.println("消费消息:-----"+reciverMessage);
        }
    }
}

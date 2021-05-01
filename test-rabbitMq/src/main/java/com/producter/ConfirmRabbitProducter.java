package com.producter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.util.GetConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ConfirmRabbitProducter {
    private static final String exchange = "test-topic-exchange";
    private static final String key = "test.topic.key";

    public static void main(String[] args) throws IOException {

        //2.创建连接
        Connection connection;
        if (!GetConnection.getConnection().isPresent())
            return;
        connection = GetConnection.getConnection().get();
        //3.创建通道
        Channel channel = connection.createChannel();
        //4.声明交换机
        channel.exchangeDeclare(exchange,"topic", false, true, null);
        //开启confirm
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {

            /***
             * 接口成功
             *
             * @param deliveryTag deliveryTag消息id
             * @param multiple 是否批量
             * @throws IOException*/
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息id"+deliveryTag+"............ack");
                System.out.println(multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息id"+deliveryTag+"............noack");
            }
        });
        //7.发布消息
        for (int i=0; i<5; i++) {
            String msg = "第"+i+"个消息";
            channel.basicPublish(exchange, key, null, msg.getBytes());
        }
    }
}

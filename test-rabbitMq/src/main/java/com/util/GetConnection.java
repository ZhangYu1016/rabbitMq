package com.util;

import com.constant.VmConstant;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class GetConnection {

    public static final Optional<Connection> getConnection() {
        Connection connection = null;
        try {
            //1.工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(VmConstant.host);
            connectionFactory.setPort(VmConstant.port);
            connectionFactory.setUsername(VmConstant.username);
            connectionFactory.setPassword(VmConstant.password);
            connectionFactory.setVirtualHost(VmConstant.visualHost);

            //2.创建连接
            connection = connectionFactory.newConnection();
            return Optional.of(connection);
        }catch (Exception e) {
            log.error("连接错误：", e);
        }
        return Optional.empty();
    }
}

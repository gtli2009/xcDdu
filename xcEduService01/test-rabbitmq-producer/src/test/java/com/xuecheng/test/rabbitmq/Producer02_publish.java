package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer02_publish {
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    public static void main(String[] args) {
        //创建工厂连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机
        connectionFactory.setVirtualHost("/");
        //创建连接
        Channel channel = null;
        Connection connection = null;
        try {
            connection = new ConnectionFactory().newConnection();
            channel = connection.createChannel();
            //创建新的会话通道//创建与RabbitMQ服务的TCP连接 connection  = factory.newConnection();
            ////创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务 channel = connection.createChannel();
            ///**
            //* 声明队列，如果Rabbit中没有此队列将自动创建
            //* param1:队列名称
            //* param2:是否持久化
            //* param3:队列是否独占此连接
            //* param4:队列不再使用时是否自动删除此队列
            //* param5:队列参数
            //*/
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            /**
             * 参数明细
             * 1、交换机名称
             *  2、交换机类型，fanout 工作模式 publish/subscribe
             *  topic  Routing 工作模式
             *  direct  Topics 工作模式
             *  headers  headers 工作模式
             */
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            //消息与路由器绑定
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            /**
             * 消息发布方法
             *	param1：Exchange的名称，如果没有指定，则使用Default  Exchange
             *   param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             *	param4：消息体
             /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显 示绑定或解除绑定
             * 默认的交换机，routingKey等于队列名称
             */
            for (int i = 0; i < 5; i++) {
                String message = "hello Rabbit ,兔子";
                channel.basicPublish(EXCHANGE_FANOUT_INFORM, "", null, message.getBytes());
                System.out.println("send to mq" + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
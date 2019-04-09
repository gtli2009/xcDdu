package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02_scribe_sms {
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂连接
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机
        connectionFactory.setVirtualHost("/");
        //创建连接
        Connection connection=connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);


        /**
         * 参数明细
         * 1、交换机名称
         *  2、交换机类型，fanout 工作模式 publish/subscribe
         *  topic  Routing 工作模式
         *  direct  Topics 工作模式
         *  headers  headers 工作模式
         */
        channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
        //绑定交换机队列
        channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");
            //实现消费方法
        DefaultConsumer defaultConsumer=new DefaultConsumer(channel){
        /**
         * 消费者接收消息调用此方法
         *	@param consumerTag 消费者的标签，在channel.basicConsume()去指定
         * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
        (收到消息失败后是否需要重新发送)
         *	@param properties 消息属性
         *	@param body 消息内容
         *	@throws IOException
            */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                String message = new String(body,"utf-8");
                System.out.println("receive message: "+message);

            }
        };
        /**
         * 监听队列String queue, boolean autoAck,Consumer callback
         * 参数明细
         * 1、队列名称
         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置 为false则需要手动回复
         *  3、消费消息的方法，消费者接收到消息后调用此方法
         */

        channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
    }
}

package com.zh.handler;

import com.rabbitmq.client.Channel;
import com.zh.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Tourist
 * @version 1.0
 * @package com.zh.handler
 * @date 2019/7/11 20:25
 * @Description
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void getReceiveEmail(String msg, Message message, Channel channel){
        System.out.println("Email:"+msg);
    }
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void getReceiveSms(String msg, Message message, Channel channel){
        System.out.println("Sms:"+msg);
    }
}

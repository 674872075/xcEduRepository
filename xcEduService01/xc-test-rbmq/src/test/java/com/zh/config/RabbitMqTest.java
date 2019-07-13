package com.zh.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tourist
 * @version 1.0
 * @package com.zh.config
 * @date 2019/7/11 20:49
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testProvider() {
        for (int i=0;i<5;i++) {
            String msg="you is a msg" + i;
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM,"inform.sms.email",msg);
            System.out.println("Send  Message  is:'"  +  msg  +  "'");
        }
    }
}


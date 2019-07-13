package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms_client.config
 * @date 2019/7/12 19:46
 * @Description
 */

/**
 * 由于cms作为页面发布方要面对很多不同站点的服务器，面对很多页面发布队列，所以这里不再配置队列，只需要
 * 配置交换机即可。
 * 在cms工程只配置交换机名称即可
 */
@Configuration
public class RabbitmqConﬁg {

    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";

    /**
     * 交换机配置使用direct类型 路由模式
     * @return the exchange
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        //durable 持久化交换机
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }
}

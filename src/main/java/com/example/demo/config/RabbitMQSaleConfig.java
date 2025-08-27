package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSaleConfig {

    public static final String Sale_QUEUE = "sale.queue";
    public static final String Sale_EXCHANGE = "sale.exchange";
    public static final String Sale_ROUTING_KEY = "sale.routingkey";

    @Bean
    public Queue saleQueue() {
        return new Queue(Sale_QUEUE, true);
    }

    @Bean
    public TopicExchange saleExchange() {
        return new TopicExchange(Sale_EXCHANGE);
    }

    @Bean
    public Binding saleBinding(Queue saleQueue, TopicExchange saleExchange) {
        return BindingBuilder.bind(saleQueue).to(saleExchange).with(Sale_ROUTING_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter2() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate2(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter2());
        return template;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory2(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter2());
        return factory;
    }
}

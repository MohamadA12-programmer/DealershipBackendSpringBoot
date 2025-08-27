package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCarConfig {
    public static final String CAR_QUEUE = "car.queue";
    public static final String CAR_EXCHANGE = "car.exchange";
    public static final String CAR_ROUTING_KEY = "car.routingKey";

    @Bean
    public Queue carQueue() {
        return new Queue(CAR_QUEUE, true);
    }

    @Bean
    public TopicExchange carExchange() {
        return new TopicExchange(CAR_EXCHANGE);
    }

    @Bean(name = "carBinding")
    public Binding carbinding(Queue carQueue, TopicExchange carExchange) {
        return BindingBuilder.bind(carQueue).to(carExchange).with(CAR_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }

}

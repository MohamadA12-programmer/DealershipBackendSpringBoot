package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQUserConfig {
    public static final String User_QUEUE = "User.queue";
    public static final String User_EXCHANGE = "User.exchange";
    public static final String User_ROUTING_KEY = "User.routingKey";

    @Bean
    public Queue UserQueue() {
        return new Queue(User_QUEUE, true);
    }

    @Bean
    public TopicExchange UserExchange() {
        return new TopicExchange(User_EXCHANGE);
    }

    @Bean(name = "UserBinding")
    public Binding bindingUser(Queue UserQueue, TopicExchange UserExchange) {
        return BindingBuilder.bind(UserQueue).to(UserExchange).with(User_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter4() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate4(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter4());
        return template;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory4(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter4());
        return factory;
    }
}

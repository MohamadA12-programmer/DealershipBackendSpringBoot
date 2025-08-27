package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCustomerConfig {
    public static final String CUSTOMER_QUEUE = "customer.queue";
    public static final String CUSTOMER_EXCHANGE = "customer.exchange";
    public static final String CUSTOMER_ROUTING_KEY = "customer.routingKey";

    @Bean
    public Queue customerQueue() {
        return new Queue(CUSTOMER_QUEUE, true);
    }

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }

    @Bean(name = "customerBinding")
    public Binding bindingCustomer(Queue customerQueue, TopicExchange customerExchange) {
        return BindingBuilder.bind(customerQueue).to(customerExchange).with(CUSTOMER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter1() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate1(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter1());
        return template;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory1(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter1());
        return factory;
    }
}

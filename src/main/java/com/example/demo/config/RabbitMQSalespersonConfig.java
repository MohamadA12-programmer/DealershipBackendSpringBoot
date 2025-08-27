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
public class RabbitMQSalespersonConfig {
    public static final String SALESPERSON_QUEUE = "salesperson.queue";
    public static final String SALESPERSON_EXCHANGE = "salesperson.exchange";
    public static final String SALESPERSON_ROUTING_KEY = "salesperson.routingKey";

    @Bean
    public Queue salespersonQueue() {
        return new Queue(SALESPERSON_QUEUE, true);
    }

    @Bean
    public TopicExchange salespersonExchange() {
        return new TopicExchange(SALESPERSON_EXCHANGE);
    }

    @Bean(name = "salespersonBinding")
    public Binding bindingSalesperson(Queue salespersonQueue, TopicExchange salespersonExchange) {
        return BindingBuilder.bind(salespersonQueue).to(salespersonExchange).with(SALESPERSON_ROUTING_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter3() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate3(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter3());
        return template;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory3(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter3());
        return factory;
    }
}

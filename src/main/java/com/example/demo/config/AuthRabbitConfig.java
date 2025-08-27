package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRabbitConfig {
    public static final String AUTH_QUEUE = "auth.queue";
    public static final String AUTH_EXCHANGE = "auth.exchange";
    public static final String AUTH_ROUTING_KEY = "auth.routingKey";

    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE, true);
    }

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(AUTH_EXCHANGE);
    }

    @Bean(name = "authBinding")
    public Binding bindingAuth(Queue authQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authQueue).to(authExchange).with(AUTH_ROUTING_KEY);
    }
}

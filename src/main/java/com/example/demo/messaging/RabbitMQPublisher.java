package com.example.demo.messaging;

import com.example.demo.POJO.*;
import com.example.demo.config.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCarEvent(CarEvent carevent) {
        rabbitTemplate.convertAndSend(
                RabbitMQCarConfig.CAR_EXCHANGE,
                RabbitMQCarConfig.CAR_ROUTING_KEY,
                carevent
        );
        System.out.println("Sent: " + carevent);
    }

    public void sendSaleEvent(SaleEvent saleEvent) {
        rabbitTemplate.convertAndSend(
                RabbitMQSaleConfig.Sale_EXCHANGE,
                RabbitMQSaleConfig.Sale_ROUTING_KEY,
                saleEvent
        );
    }

    public void sendCustomerEvent(CustomerEvent customerEvent) {
        rabbitTemplate.convertAndSend(
                RabbitMQCustomerConfig.CUSTOMER_EXCHANGE,
                RabbitMQCustomerConfig.CUSTOMER_ROUTING_KEY,
                customerEvent
        );
    }

    public void sendSalespersonEvent(SalesPersonEvent salesPersonEvent) {
        rabbitTemplate.convertAndSend(
                RabbitMQSalespersonConfig.SALESPERSON_EXCHANGE,
                RabbitMQSalespersonConfig.SALESPERSON_ROUTING_KEY,
                salesPersonEvent
        );
    }

    public void sendUserEvent(UserEvent userEvent){
        rabbitTemplate.convertAndSend(RabbitMQUserConfig.User_QUEUE, RabbitMQUserConfig.User_QUEUE,
                userEvent);
    }

}

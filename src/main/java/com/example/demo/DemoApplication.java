package com.example.demo;

import com.example.demo.POJO.CarEvent;
import com.example.demo.messaging.RabbitMQPublisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	//@Bean
	//public CommandLineRunner runner(RabbitMQPublisher producer) {
		//return args -> {
			//for (int i = 1; i <= 20; i++) {
				//CarEvent event = new CarEvent();
				//event.setEventType("CAR_CREATED");
				//event.setCarId(i);
				//event.setMake("Make-" + i);
				//event.setModel("Model-" + i);

				//producer.sendCarEvent(event);
			//}
		//};
	//}

}
